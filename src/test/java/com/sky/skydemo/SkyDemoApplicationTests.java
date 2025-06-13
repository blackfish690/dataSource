package com.sky.skydemo;

import com.sky.skydemo.utils.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class SkyDemoApplicationTests {

    @Test
    void countdownFromGetOffWork() {
        // 当前时间
        LocalTime now = LocalTime.now();

        // 下班时间 17:30
        LocalTime offWorkTime = LocalTime.of(17, 30);

        // 判断是否已过下班时间
        if (now.isAfter(offWorkTime)) {
            System.out.println("今天已过下班时间");
            return;
        }

        // 计算剩余时间
        Duration duration = Duration.between(now, offWorkTime);
        long minutesLeft = duration.toMinutes();

        System.out.println("距离下班还有 " + minutesLeft + " 分钟");

    }

    @Test
    void countdownFromGetOffWorkHour() {
        // 当前时间
        LocalTime now = LocalTime.now();

        // 下班时间 17:30
        LocalTime offWorkTime = LocalTime.of(17, 30);

        // 判断是否已过下班时间
        if (now.isAfter(offWorkTime)) {
            System.out.println("今天已过下班时间");
            return;
        }

        // 计算剩余时间
        Duration duration = Duration.between(now, offWorkTime);
        long minutesLeft = duration.toMinutes();

        // 转换为小时和分钟
        long hours = minutesLeft / 60;
        long minutes = minutesLeft % 60;

        System.out.printf("距离下班还有 %d 小时 %d 分钟%n", hours, minutes);
    }


    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        System.out.println("test");
    }

    @Test
    void test2() {
        System.out.println("冲突2");
    }

    @Test
    void test5() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = sdf.parse("2024-02-15");
        String belongingMonth = DateUtil.getBelongingMonth(parsedDate, 31);
        System.out.println("归属月份 = " + belongingMonth);
    }


    public static String generateStatementSn(String maxSn) {
        // 获取当前年月，格式 yyMM
        String prefix = new SimpleDateFormat("yyMM").format(new Date());

        int nextNumber = 1;
        if (maxSn != null && maxSn.startsWith(prefix)) {
            // 解析后缀数字
            String numberPart = maxSn.substring(5); // 去掉"2405-"的前缀
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // 异常则保持 nextNumber = 1
            }
        }

        // 格式化后缀：001 ~ 999 或 1000 开始
        String newNumberPart = nextNumber < 1000
                ? String.format("%03d", nextNumber)
                : Integer.toString(nextNumber);

        return prefix + newNumberPart;
    }

    @Test
    void test3() {
        String maxSn = "2505001";
        String newSn = generateStatementSn(maxSn);
        System.out.println("newSn = " + newSn);
    }

    /**
     * 计算对账日期
     *
     * @param periodStr 货款归属格式为 yyyy/MM，例如 2025/03
     * @param cycleDays 结算周期（必须为30的倍数），例如 0, 30, 60
     * @return 对账日期（对应周期后的月末日期，返回 java.util.Date）
     */
    public static Date getReconciliationDate(String periodStr, int cycleDays) {
        // 校验必须为30的倍数
        if (cycleDays % 30 != 0) {
            throw new IllegalArgumentException("结算周期必须是30的倍数");
        }

        // 解析格式为 yyyy/MM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
        YearMonth baseMonth = YearMonth.parse(periodStr, formatter);

        // 计算应加月份数量：30天为一个月
        int monthsToAdd = cycleDays / 30;

        // 推算目标月
        YearMonth targetMonth = baseMonth.plusMonths(monthsToAdd);

        // 获取目标月的最后一天
        LocalDate endOfMonth = targetMonth.atEndOfMonth();

        // 转换为 java.util.Date
        return Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) {
        String period = "2025/03";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        System.out.println("结算周期为 0 天，对账日期为: " + sdf.format(getReconciliationDate(period, 0)));
        System.out.println("结算周期为 30 天，对账日期为: " + sdf.format(getReconciliationDate(period, 30)));
        System.out.println("结算周期为 60 天，对账日期为: " + sdf.format(getReconciliationDate(period, 60)));

    }

    @Test
    void test4() {
        Date today = new Date(); // 当前时间

        // 中文注释：加上 5 天，返回 Date 类型
        Date future = DateUtil.addDays(today, 5);
        System.out.println("加 5 天后的日期：" + future);

        // 中文注释：加上 -7 天，返回格式化字符串
        String pastStr = DateUtil.addDaysFormat(today, -7);
        System.out.println("7 天前的日期字符串：" + pastStr);
    }

    @Test
    void test6() {
//        List<MonthData> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        YearMonth currentMonth = YearMonth.now();
        final int HISTORY_MONTHS = 12;

        // 获取今年的月，排除本月
        for (int i = 1; i <= HISTORY_MONTHS; i++) {
            YearMonth yearMonth = currentMonth.minusMonths(i);
            String yearMonthStr = yearMonth.format(formatter);
            // TODO: 后续处理 yearMonthStr
        }

        //添加上年12个月份
        List<String> lastYearMonths = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
//            YearMonth ym = YearMonth.of(lastYear, month);
//            lastYearMonths.add(ym.format(formatter));
        }

    }

    @Test
    void test7() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth currentMonth = YearMonth.now(); // 当前月份
        int currentYear = currentMonth.getYear();

        List<String> yearMonthList = new ArrayList<>();

        // 从1月到上一个月（不包含当前月）
        for (int month = 1; month < currentMonth.getMonthValue(); month++) {
            YearMonth ym = YearMonth.of(currentYear, month);
            String s = String.valueOf(ym);
            yearMonthList.add(ym.format(formatter));
        }


        // 获取去年的 12 个月（完整一年）
        int lastYear = currentYear - 1;
        List<String> lastYearMonths = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(lastYear, month);
            lastYearMonths.add(ym.format(formatter));
        }

        // 输出测试
        System.out.println("过去12个月（不含本月）:");
        yearMonthList.forEach(System.out::println);

        System.out.println("去年的12个月:");
        lastYearMonths.forEach(System.out::println);
    }

    @Test
    void test8() {
        ArrayList<String> strings = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        YearMonth currentMonth = YearMonth.now();
        // 往前推12个月
        for (int i = 0; i < 18; i++) {
            YearMonth yearMonth = currentMonth.minusMonths(i);
            String yearMonthStr = yearMonth.format(formatter);
            strings.add(yearMonthStr);
        }
        System.out.println(strings);
    }

}
