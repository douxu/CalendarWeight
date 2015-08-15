package constant;

import java.util.TimeZone;

public class Calendarconstant {
	public final static String[] sch_type = { "会议", "约会", "电话", "纪念日", "生日", "课程", "其他" }; // 日程类型
	public final static String[] remind = {"提醒一次","隔10分钟","隔30分钟","隔一小时","每天重复","每周重复","每月重复","每年重复"};
    public final static TimeZone tz1 = TimeZone.getTimeZone("GMT+8");
    public final static String monday = "今天是周一，请努力学习";
    public final static String tuesday = "今天是周二，请努力学习";
    public final static String wednesday = "今天是周三，请努力学习";
    public final static String thursday = "今天是周四，请努力学习";
    public final static String friday = "今天是周五，请努力学习";
    public final static String saturday = "今天是周六，休息一下吧";
    public final static String sunday = "今天是周日，休息一下吧";
}
