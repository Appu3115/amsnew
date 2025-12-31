package com.example.amsnew.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.amsnew.dto.AdminAttendanceDTO;
import com.example.amsnew.dto.AdminAttendanceResponse;
import com.example.amsnew.dto.DepartmentAttendanceDTO;
import com.example.amsnew.dto.EmployeeAttendanceResponse;
import com.example.amsnew.model.ActivityPulse;
import com.example.amsnew.model.ActivityType;
import com.example.amsnew.model.Attendance;
import com.example.amsnew.model.AttendanceStatus;
import com.example.amsnew.model.DailyAttendanceStatus;
import com.example.amsnew.model.DailyStatus;
import com.example.amsnew.model.Department;
import com.example.amsnew.model.Employees;
import com.example.amsnew.model.SessionType;
import com.example.amsnew.model.Shift;
import com.example.amsnew.model.WorkSession;
import com.example.amsnew.repository.ActivityPulseRepository;
import com.example.amsnew.repository.AttendanceRepository;
import com.example.amsnew.repository.DailyAttendanceStatusRepo;
import com.example.amsnew.repository.DepartmentRepository;
import com.example.amsnew.repository.UserRepository;
import com.example.amsnew.repository.WorkSessionRepository;
import com.example.amsnew.util.DateUtil;


@Service
public class AttendanceService {
       @Autowired
       private AttendanceRepository attendanceRepo;
       
       @Autowired
    	private UserRepository userrepo;
       
       @Autowired
       private DepartmentRepository departmentRepo;

//       @Autowired
//       private ShiftRepo shiftRepo;
       
       @Autowired
       private WorkSessionRepository  workSessionRepo;
       
       @Autowired
       private DailyAttendanceStatusRepo dailyStatusRepo;
       
       @Autowired
       private ActivityPulseRepository  activityPulseRepo;
 

       public ResponseEntity<?> login(String employeeId, boolean workFromHome) {

    	    if (employeeId == null || employeeId.isBlank()) {
    	        return ResponseEntity.badRequest().body("Employee ID is required");
    	    }

    	    LocalDate today = LocalDate.now();
    	    LocalDateTime now = LocalDateTime.now();

    	    // 1️⃣ Fetch employee
    	    Employees emp = userrepo.findByEmployeeId(employeeId)
    	            .orElseThrow(() -> new RuntimeException("Employee not found"));

    	    Shift shift = emp.getShift();
    	    if (shift == null) {
    	        return ResponseEntity.badRequest().body("Shift not assigned. Contact admin.");
    	    }

    	    // 2️⃣ Prevent multiple punch-ins for the same day
    	    boolean alreadyLoggedIn =
    	            attendanceRepo.findByEmployee_EmployeeIdAndAttendanceDate(employeeId, today)
    	                    .isPresent();

    	    if (alreadyLoggedIn) {
    	        return ResponseEntity.badRequest().body("Already logged in today");
    	    }

    	    // 3️⃣ Calculate late minutes
    	    LocalTime shiftStartWithGrace =
    	            shift.getStartTime().plusMinutes(shift.getGraceMinutes());

    	    long lateMinutes = 0;
    	    if (now.toLocalTime().isAfter(shiftStartWithGrace)) {
    	        lateMinutes = Duration
    	                .between(shiftStartWithGrace, now.toLocalTime())
    	                .toMinutes();
    	    }

    	    // 4️⃣ Create Attendance (RAW PUNCH DATA)
    	    Attendance attendance = new Attendance();
    	    attendance.setEmployee(emp);
    	    attendance.setShift(shift);
    	    attendance.setAttendanceDate(today);
    	    attendance.setLogin(now);
    	    attendance.setLateMinutes(lateMinutes);
    	    attendance.setStatus(AttendanceStatus.PRESENT); // ✅ ALWAYS PRESENT if logged in

    	    Attendance savedAttendance = attendanceRepo.save(attendance);

    	    // 5️⃣ Start WORK session
    	    WorkSession workSession = new WorkSession();
    	    workSession.setEmployee(emp);
    	    workSession.setAttendance(savedAttendance);
    	    workSession.setStartTime(now);
    	    workSession.setSessionType(SessionType.WORK);
    	    workSessionRepo.save(workSession);

    	    // 6️⃣ Create Daily Attendance Status (FINAL DAILY VIEW)
    	    DailyAttendanceStatus daily = new DailyAttendanceStatus();
    	    daily.setEmployee(emp);
    	    daily.setAttendance(savedAttendance);
    	    daily.setAttendanceDate(today);
    	    daily.setLoginTime(now);
    	    daily.setLateMinutes(lateMinutes);

    	    daily.setStatus(
    	            workFromHome
    	                    ? DailyStatus.WORK_FROM_HOME
    	                    : DailyStatus.PRESENT
    	    );

    	    dailyStatusRepo.save(daily);

    	    // 7️⃣ Response
    	    return ResponseEntity.ok(
    	            Map.of(
    	                    "message", "Login successful",
    	                    "employeeId", employeeId,
    	                    "loginTime", now,
    	                    "present", true,
    	                    "lateMinutes", lateMinutes,
    	                    "dailyStatus", daily.getStatus()
    	            )
    	    );
    	}



       public ResponseEntity<?> logoutByEmployeeId(String employeeId) {

    	    if (employeeId == null || employeeId.isBlank()) {
    	        return ResponseEntity.badRequest().body("Employee ID is required");
    	    }

    	    // 1️⃣ Find active attendance (logged in but not logged out)
    	    Attendance attendance = attendanceRepo
    	            .findByEmployee_EmployeeIdAndLogoutIsNull(employeeId)
    	            .orElseThrow(() -> new RuntimeException("No active login found"));


    	    Shift shift = attendance.getShift();
    	    if (shift == null) {
    	        return ResponseEntity.badRequest().body("Shift not assigned");
    	    }

    	    LocalDateTime now = LocalDateTime.now();

    	    // 2️⃣ Calculate shift end (handles night shift also)
    	    LocalDateTime shiftEnd = LocalDateTime.of(
    	            attendance.getAttendanceDate(),
    	            shift.getEndTime()
    	    );

    	    if (shift.getEndTime().isBefore(shift.getStartTime())) {
    	        shiftEnd = shiftEnd.plusDays(1);
    	    }

    	    // 3️⃣ Logout (ALLOWED ANYTIME)
    	    attendance.setLogout(now);

    	    // ✅ Attendance remains PRESENT (do NOT change to LOGGED_OUT)
    	    attendance.setStatus(AttendanceStatus.PRESENT);

    	    // 4️⃣ Calculate overtime (only if after shift end)
    	    long overtimeMinutes = 0;
    	    if (now.isAfter(shiftEnd)) {
    	        overtimeMinutes = Duration.between(shiftEnd, now).toMinutes();
    	    }
    	    attendance.setOvertimeMinutes(overtimeMinutes);

    	    attendanceRepo.save(attendance);

    	    // 5️⃣ Close active work session
    	    WorkSession activeSession = workSessionRepo
    	            .findByEmployee_EmployeeIdAndEndTimeIsNull(employeeId)
    	            .orElseThrow(() -> new RuntimeException("No active work session"));

    	    activeSession.setEndTime(now);
    	    workSessionRepo.save(activeSession);

    	    // 6️⃣ Update Daily Attendance Status
    	    DailyAttendanceStatus daily = dailyStatusRepo
    	            .findByEmployeeAndAttendanceDate(
    	                    attendance.getEmployee(),
    	                    attendance.getAttendanceDate()
    	            )
    	            .orElseThrow(() -> new RuntimeException("Daily status not found"));

    	    daily.setLogoutTime(now);
    	    daily.setOvertimeMinutes(overtimeMinutes);

    	    // 7️⃣ Calculate totals
    	    Duration totalWork =
    	            calculateTotalWorkTime(employeeId, attendance.getAttendanceDate());

    	    Duration totalBreak =
    	            calculateTotalBreakTime(employeeId, attendance.getAttendanceDate());

    	    daily.setTotalWorkMinutes(totalWork.toMinutes());
    	    daily.setTotalBreakMinutes(totalBreak.toMinutes());

    	    dailyStatusRepo.save(daily);

    	    // 8️⃣ Response
    	    return ResponseEntity.ok(
    	            Map.of(
    	                    "message", "Logout successful",
    	                    "logoutTime", now,
    	                    "overtimeMinutes", overtimeMinutes,
    	                    "totalWorkMinutes", totalWork.toMinutes()
    	            )
    	    );
    	}


       public ResponseEntity<?> fetchAttendance(String employeeId, String date) {

    	    LocalDate parsedDate = null;

    	    if (date != null && !date.isBlank()) {
    	        parsedDate = DateUtil.parseDate(date);
    	    }

    	    // ================= CASE 1: employeeId + date (Dashboard) =================
    	    if (employeeId != null && parsedDate != null) {

    	        Optional<Attendance> attendanceOpt =
    	                attendanceRepo.findByEmployee_EmployeeIdAndAttendanceDate(
    	                        employeeId, parsedDate);

    	        Optional<DailyAttendanceStatus> dailyOpt =
    	                dailyStatusRepo.findByEmployee_EmployeeIdAndAttendanceDate(
    	                        employeeId, parsedDate);

    	        if (attendanceOpt.isEmpty() && dailyOpt.isEmpty()) {
    	            return ResponseEntity.badRequest().body(
    	                    "No attendance found for employeeId: " +
    	                    employeeId + " on date: " + parsedDate
    	            );
    	        }

    	        Map<String, Object> response = new HashMap<>();
    	        response.put("attendance", attendanceOpt.orElse(null));
    	        response.put("dailyStatus", dailyOpt.orElse(null));

    	        return ResponseEntity.ok(response);
    	    }

    	    // ================= CASE 2: employeeId only =================
    	    if (employeeId != null) {

    	        List<Attendance> list =
    	                attendanceRepo.findByEmployee_EmployeeId(employeeId);

    	        if (list.isEmpty()) {
    	            return ResponseEntity.badRequest().body(
    	                    "No attendance found for employeeId: " + employeeId
    	            );
    	        }

    	        return ResponseEntity.ok(list);
    	    }

    	    // ================= CASE 3: date only =================
    	    if (parsedDate != null) {

    	        List<Attendance> list =
    	                attendanceRepo.findByAttendanceDate(parsedDate);

    	        if (list.isEmpty()) {
    	            return ResponseEntity.badRequest().body(
    	                    "No attendance found for date: " + parsedDate
    	            );
    	        }

    	        return ResponseEntity.ok(list);
    	    }

    	    // ================= CASE 4: no filters =================
    	    List<Attendance> list = attendanceRepo.findAll();

    	    if (list.isEmpty()) {
    	        return ResponseEntity.ok(List.of());
    	    }

    	    return ResponseEntity.ok(list);
    	}

       
       public List<DepartmentAttendanceDTO> getDepartmentWiseAttendance(LocalDate date) {

    	    List<Department> departments = departmentRepo.findAll();
    	    List<DepartmentAttendanceDTO> result = new ArrayList<>();

    	    for (Department dept : departments) {

    	        long totalEmployees =
    	                userrepo.countByDepartmentId(dept.getId());

    	        // ✅ PRESENT = attendance exists
    	        long present =
    	                attendanceRepo.countByEmployee_Department_IdAndAttendanceDate(
    	                        dept.getId(), date
    	                );

    	        // ✅ LATE = present + lateMinutes > 0
    	        long late =
    	                attendanceRepo.countByEmployee_Department_IdAndAttendanceDateAndLateMinutesGreaterThan(
    	                        dept.getId(), date, 0L
    	                );

    	        long absent = totalEmployees - present;

    	        result.add(
    	                new DepartmentAttendanceDTO(
    	                        dept.getDeptName(),
    	                        totalEmployees,
    	                        present,
    	                        late,
    	                        absent
    	                )
    	        );
    	    }

    	    return result;
    	}


        // BREAK, LUNCH 
       public ResponseEntity<?> startPause(String employeeId, SessionType type) {

           WorkSession active = workSessionRepo
                   .findByEmployee_EmployeeIdAndEndTimeIsNull(employeeId)
                   .orElseThrow(() -> new RuntimeException("No active session"));

           active.setEndTime(LocalDateTime.now());
           workSessionRepo.save(active);

           WorkSession pause = new WorkSession();
           pause.setEmployee(active.getEmployee());
           pause.setAttendance(active.getAttendance());
           pause.setStartTime(LocalDateTime.now());
           pause.setSessionType(type); // BREAK or LUNCH

           workSessionRepo.save(pause);

           return ResponseEntity.ok(type + " started");
       }
       
       // RESUME WORK
       public ResponseEntity<?> resumeWork(String employeeId) {

           WorkSession active = workSessionRepo
                   .findByEmployee_EmployeeIdAndEndTimeIsNull(employeeId)
                   .orElseThrow(() -> new RuntimeException("No active pause"));

           active.setEndTime(LocalDateTime.now());
           workSessionRepo.save(active);

           WorkSession work = new WorkSession();
           work.setEmployee(active.getEmployee());
           work.setAttendance(active.getAttendance());
           work.setStartTime(LocalDateTime.now());
           work.setSessionType(SessionType.WORK);

           workSessionRepo.save(work);

           return ResponseEntity.ok("Work resumed");
       }
       
       // ACTIVITY TRACKING
       public void recordActivity(String employeeId, ActivityType type) {

           // Only record during WORK
           WorkSession active = workSessionRepo
                   .findByEmployee_EmployeeIdAndEndTimeIsNull(employeeId)
                   .orElse(null);

           if (active == null || active.getSessionType() != SessionType.WORK) {
               return;
           }

           ActivityPulse pulse = new ActivityPulse();
           pulse.setEmployeeId(employeeId);
           pulse.setPulseTime(LocalDateTime.now());
           pulse.setActivityType(type);

           activityPulseRepo.save(pulse);
       }
       
       // PRODUCTIVE TIME
       
       private static final long IDLE_MINUTES = 2;

       public Duration calculateProductiveTime(String employeeId, LocalDate date) {

           LocalDateTime start = date.atStartOfDay();
           LocalDateTime end = date.atTime(23, 59, 59);

           List<ActivityPulse> pulses =
                   activityPulseRepo.findPulses(employeeId, start, end);

           Duration productive = Duration.ZERO;

           for (int i = 0; i < pulses.size() - 1; i++) {
               Duration gap = Duration.between(
                       pulses.get(i).getPulseTime(),
                       pulses.get(i + 1).getPulseTime());

               if (gap.toMinutes() <= IDLE_MINUTES) {
                   productive = productive.plus(gap);
               }
           }
           return productive;
       }
       
       
    // TOTAL WORK TIME
       public Duration calculateTotalWorkTime(String employeeId, LocalDate date) {

           LocalDateTime start = date.atStartOfDay();
           LocalDateTime end = date.atTime(23, 59, 59);

           List<WorkSession> sessions =
                   workSessionRepo.findWorkSessions(employeeId, start, end);

           Duration totalWork = Duration.ZERO;

           for (WorkSession ws : sessions) {
               if (ws.getEndTime() != null) {
                   totalWork = totalWork.plus(
                           Duration.between(ws.getStartTime(), ws.getEndTime())
                   );
               }
           }
           return totalWork;
       }

    // IDLE TIME
       public Duration calculateIdleTime(String employeeId, LocalDate date) {

           Duration totalWork = calculateTotalWorkTime(employeeId, date);
           Duration productive = calculateProductiveTime(employeeId, date);

           if (productive.compareTo(totalWork) > 0) {
               return Duration.ZERO;
           }
           return totalWork.minus(productive);
       }
       
       
       
    // TOTAL BREAK TIME (BREAK + LUNCH together)
       public Duration calculateTotalBreakTime(String employeeId, LocalDate date) {

           LocalDateTime start = date.atStartOfDay();
           LocalDateTime end = date.atTime(23, 59, 59);

           List<SessionType> breakTypes = List.of(
                   SessionType.BREAK,
                   SessionType.LUNCH
           );

           List<WorkSession> breakSessions =
                   workSessionRepo.findByEmployee_EmployeeIdAndSessionTypeInAndStartTimeBetween(
                           employeeId,
                           breakTypes,
                           start,
                           end
                   );

           Duration totalBreak = Duration.ZERO;

           for (WorkSession ws : breakSessions) {
               if (ws.getEndTime() != null) {
                   totalBreak = totalBreak.plus(
                           Duration.between(ws.getStartTime(), ws.getEndTime())
                   );
               }
           }

           return totalBreak;
       }

       //Permission request
       public ResponseEntity<?> requestPermission( String employeeId, long minutes) {

    	    Employees emp = userrepo.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));

    	    LocalDate today = LocalDate.now();

    	    DailyAttendanceStatus daily =dailyStatusRepo.findByEmployeeAndAttendanceDate(emp, today)
    	            .orElseGet(() -> {
    	                DailyAttendanceStatus d = new DailyAttendanceStatus();
    	                d.setEmployee(emp);
    	                d.setAttendanceDate(today);
    	                d.setStatus(DailyStatus.PRESENT); // IMPORTANT
    	                return d;
    	            });

    	    daily.setPermissionMinutes(minutes);

    	    dailyStatusRepo.save(daily);

    	    return ResponseEntity.ok("Permission recorded");
    	}
       
    // Automatic Absent mark
       @Scheduled(cron = "0 * * * * ?") // every minute
       public void autoMarkAbsentAfterOneHour() {

           LocalDate today = LocalDate.now();
           LocalDateTime now = LocalDateTime.now();

           List<Employees> employees = userrepo.findAll();

           for (Employees emp : employees) {

               Shift shift = emp.getShift();
               if (shift == null) continue;

               // Shift start today
               LocalDateTime shiftStart =
                       LocalDateTime.of(today, shift.getStartTime());

               // 1 hour grace to mark absent
               LocalDateTime absentCutoff = shiftStart.plusHours(1);

               // Too early → skip
               if (now.isBefore(absentCutoff)) {
                   continue;
               }

               // Already has daily status?
               Optional<DailyAttendanceStatus> dailyOpt =
                       dailyStatusRepo.findByEmployeeAndAttendanceDate(emp, today);

               if (dailyOpt.isPresent()) {
                   // If permission exists → do not mark absent
                   DailyAttendanceStatus daily = dailyOpt.get();
                   if (daily.getPermissionMinutes() != null &&
                       daily.getPermissionMinutes() > 0) {
                       continue;
                   }
                   // Any daily status already exists → skip
                   continue;
               }

               // Check if employee logged in today
               boolean loggedIn =
                       attendanceRepo
                               .findByEmployee_EmployeeIdAndAttendanceDate(
                                       emp.getEmployeeId(), today)
                               .isPresent();

               // If NOT logged in → mark ABSENT
               if (!loggedIn) {

                   DailyAttendanceStatus absent = new DailyAttendanceStatus();
                   absent.setEmployee(emp);
                   absent.setAttendanceDate(today);
                   absent.setStatus(DailyStatus.ABSENT);

                   // Explicit nulls / zeros for safety
                   absent.setLoginTime(null);
                   absent.setLogoutTime(null);
                   absent.setLateMinutes(0L);
                   absent.setOvertimeMinutes(0L);
                   absent.setTotalWorkMinutes(0L);
                   absent.setTotalBreakMinutes(0L);
                   absent.setPermissionMinutes(0L);

                   dailyStatusRepo.save(absent);
               }
           }
       }

       
       
       public ResponseEntity<?> getAllEmployeesDailyAttendance(
    	        String fromDate,
    	        String toDate
    	) {

    	    LocalDate start = null;
    	    LocalDate end = null;

    	    if (fromDate != null && !fromDate.isBlank()) {
    	        start = DateUtil.parseDate(fromDate);
    	    }
    	    if (toDate != null && !toDate.isBlank()) {
    	        end = DateUtil.parseDate(toDate);
    	    }

    	    List<DailyAttendanceStatus> list;

    	    if (start != null && end != null) {
    	        list = dailyStatusRepo.findByAttendanceDateBetween(start, end);
    	    } else if (start != null) {
    	        list = dailyStatusRepo.findByAttendanceDate(start);
    	    } else {
    	        list = dailyStatusRepo.findAll();
    	    }

    	    if (list.isEmpty()) {
    	        return ResponseEntity.ok(Collections.emptyList());
    	    }

    	    List<AdminAttendanceDTO> response = new ArrayList<>();

    	    for (DailyAttendanceStatus d : list) {

    	        response.add(
    	                new AdminAttendanceDTO(
    	                        d.getEmployee().getEmployeeId(),
    	                        d.getEmployee().getFirstName(),
    	                        d.getAttendanceDate(),
    	                        d.getLoginTime(),
    	                        d.getLogoutTime(),
    	                        d.getStatus(),
    	                        d.getLateMinutes(),
    	                        d.getOvertimeMinutes(),
    	                        d.getTotalWorkMinutes(),
    	                        d.getTotalBreakMinutes(),
    	                        d.getPermissionMinutes()
    	                )
    	        );
    	    }

    	    return ResponseEntity.ok(response);
    	}


       // EMPLOYEE ATTENDANCE SUMMARY
       public ResponseEntity<?> getEmployeeAttendanceSummary(
    	        String employeeId,
    	        int year,
    	        int month) {

    	    LocalDate startDate = LocalDate.of(year, month, 1);
    	    LocalDate endDate = startDate.withDayOfMonth(
    	            startDate.lengthOfMonth());

    	    long totalDays =
    	        dailyStatusRepo
    	            .countByEmployee_EmployeeIdAndAttendanceDateBetween(
    	                employeeId, startDate, endDate);

    	    long presentDays =
    	        dailyStatusRepo
    	            .countByEmployee_EmployeeIdAndAttendanceDateBetweenAndStatus(
    	                employeeId, startDate, endDate, DailyStatus.PRESENT);

    	    long absentDays =
    	        dailyStatusRepo
    	            .countByEmployee_EmployeeIdAndAttendanceDateBetweenAndStatus(
    	                employeeId, startDate, endDate, DailyStatus.ABSENT);

    	    long wfhDays =
    	        dailyStatusRepo
    	            .countByEmployee_EmployeeIdAndAttendanceDateBetweenAndStatus(
    	                employeeId, startDate, endDate, DailyStatus.WORK_FROM_HOME);

    	    return ResponseEntity.ok(
    	        Map.of(
    	            "employeeId", employeeId,
    	            "year", year,
    	            "month", month,
    	            "totalDays", totalDays,
    	            "presentDays", presentDays,
    	            "absentDays", absentDays,
    	            "wfhDays", wfhDays
    	        )
    	    );
    	}


       //ATENDACE SUMMARY FOR ADMIN
       public ResponseEntity<?> getAdminAttendanceDashboard(LocalDate date) {

    	    LocalDate targetDate = (date != null) ? date : LocalDate.now();

    	    long totalEmployees = userrepo.count();

    	    long present =
    	            dailyStatusRepo.countByAttendanceDateAndStatus(
    	                    targetDate, DailyStatus.PRESENT);

    	    long absent =
    	            dailyStatusRepo.countByAttendanceDateAndStatus(
    	                    targetDate, DailyStatus.ABSENT);

    	    long wfh =
    	            dailyStatusRepo.countByAttendanceDateAndStatus(
    	                    targetDate, DailyStatus.WORK_FROM_HOME);

    	    return ResponseEntity.ok(
    	        Map.of(
    	            "date", targetDate,
    	            "totalEmployees", totalEmployees,
    	            "present", present,
    	            "absent", absent,
    	            "workFromHome", wfh
    	        )
    	    );
       }
       

       public List<EmployeeAttendanceResponse> getEmployeeAttendance(String employeeId) {

           List<Attendance> attendanceList =
                   attendanceRepo.findByEmployee_EmployeeIdOrderByAttendanceDateDesc(employeeId);

           List<DailyAttendanceStatus> dailyList =
        		   dailyStatusRepo.findByEmployee_EmployeeIdOrderByAttendanceDateDesc(employeeId);

           // Map DailyStatus by date for quick lookup
           Map<LocalDate, DailyAttendanceStatus> dailyMap =
                   dailyList.stream()
                           .collect(Collectors.toMap(
                                   DailyAttendanceStatus::getAttendanceDate,
                                   d -> d
                           ));

           List<EmployeeAttendanceResponse> response = new ArrayList<>();

           for (Attendance attendance : attendanceList) {

               EmployeeAttendanceResponse dto = new EmployeeAttendanceResponse();
               dto.setAttendanceDate(attendance.getAttendanceDate());

               // Attendance
               dto.setLogin(attendance.getLogin());
               dto.setLogout(attendance.getLogout());
               dto.setLateMinutes(attendance.getLateMinutes());
               dto.setOvertimeMinutes(attendance.getOvertimeMinutes());
               dto.setAttendanceStatus(attendance.getStatus());

               // Daily status
               DailyAttendanceStatus daily =
                       dailyMap.get(attendance.getAttendanceDate());

               if (daily != null) {
                   dto.setDailyStatus(daily.getStatus());
                   dto.setTotalWorkMinutes(daily.getTotalWorkMinutes());
                   dto.setTotalBreakMinutes(daily.getTotalBreakMinutes());
                   dto.setPermissionMinutes(daily.getPermissionMinutes());
               }

               response.add(dto);
           }

           return response;
       }
       
       public List<AdminAttendanceResponse> getAllEmployeesAttendance() {

           List<Attendance> attendanceList =
                   attendanceRepo.findAllByOrderByAttendanceDateDesc();

           List<DailyAttendanceStatus> dailyList =
        		   dailyStatusRepo.findAll();

           // 🔹 Map using employeeId + date as key
           Map<String, DailyAttendanceStatus> dailyMap =
                   dailyList.stream()
                           .collect(Collectors.toMap(
                                   d -> d.getEmployee().getEmployeeId()
                                           + "_" + d.getAttendanceDate(),
                                   d -> d
                           ));

           List<AdminAttendanceResponse> response = new ArrayList<>();

           for (Attendance attendance : attendanceList) {

               AdminAttendanceResponse dto = new AdminAttendanceResponse();

               Employees emp = attendance.getEmployee();

               dto.setEmployeeId(emp.getEmployeeId());
               dto.setEmployeeName(emp.getFirstName() + " " + emp.getLastName());

               dto.setAttendanceDate(attendance.getAttendanceDate());

               // Attendance
               dto.setLogin(attendance.getLogin());
               dto.setLogout(attendance.getLogout());
               dto.setLateMinutes(attendance.getLateMinutes());
               dto.setOvertimeMinutes(attendance.getOvertimeMinutes());
               dto.setAttendanceStatus(attendance.getStatus());

               // Daily status
               String key = emp.getEmployeeId()
                       + "_" + attendance.getAttendanceDate();

               DailyAttendanceStatus daily = dailyMap.get(key);

               if (daily != null) {
                   dto.setDailyStatus(daily.getStatus());
                   dto.setTotalWorkMinutes(daily.getTotalWorkMinutes());
                   dto.setTotalBreakMinutes(daily.getTotalBreakMinutes());
                   dto.setPermissionMinutes(daily.getPermissionMinutes());
               }

               response.add(dto);
           }

           return response;
       }
 }


