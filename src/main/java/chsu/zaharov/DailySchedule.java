package chsu.zaharov;

public class DailySchedule
{
	private String _classDate;
	private String _classStartTime;
	private String _classEndTime;
	private String _className;
	private String _classAddress;
	private String _teacherName;

	DailySchedule()
	{

	}

	DailySchedule(String classDate, String classStartTime, String classEndTime, String className, String teacherName, String classAddress)
	{
		_classDate = classDate;
		_classStartTime = classStartTime;
		_classEndTime = classEndTime;
		_className = className;
		_teacherName = teacherName;
		_classAddress = classAddress;
	}

	public void setClassDate(String classDate)
	{
		_classDate = classDate;
	}

	public void setClassStartTime(String classStartTime)
	{
		_classStartTime = classStartTime;
	}

	public void setClassEndTime(String classEndTime)
	{
		_classEndTime = classEndTime;
	}

	public void setClassName(String className)
	{
		_className = className;
	}

	public void setTeacherName(String teacherName)
	{
		_teacherName = teacherName;
	}

	public void setClassAddress(String classAddress)
	{
		_classAddress = classAddress;
	}

	public String getClassDate()
	{
		return _classDate;
	}
	public String getClassStartTime()
	{
		return _classStartTime;
	}
	public String getClassEndTime()
	{
		return _classEndTime;
	}
	public String getClassName()
	{
		return _className;
	}

	public String getTeacherName()
	{
		return _teacherName;
	}

	public String getClassAddress()
	{
		return _classAddress;
	}
}
