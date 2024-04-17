package chsu.zaharov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		String path = args[0];
		//Расписание занятий
		ArrayList<DailySchedule> schedule = new ArrayList<DailySchedule>();
		//Открываем HTML файл с информацией о занятиях
		File input = new File(path);
		Document doc = Jsoup.parse(input, "UTF-8");
		//Получаем блоки в которых записаны дни и занятия
		Elements ScheduleHTML = doc.getElementsByClass("raspisane_week_day_block");

		//Шаблоны для поиска подстрок со временем и датой занятий
		Pattern classTimePattern = Pattern.compile("[0-9]{2}:[0-9]{2}");
		Pattern classDatePattern = Pattern.compile("[0-9]{2}.[0-9]{2}.[0-9]{4}");

		//Обходим дни
		for (var i : ScheduleHTML)
		{
			String classDate = new String();
			Matcher matcher = classDatePattern.matcher(i.children().select(".raspisane_week_day_title").text());
			if (matcher.find())
			{
				classDate = matcher.group();
			}
			else
				System.out.println("ERROR: Could not find class date");
			//Получаем строку таблицы из блока с расписанием
			var ClassData = i.getElementsByTag("td");

			String classStartTime = new String();
			String classEndTime = new String();
			String className = new String();
			String classAddress = new String();
			String teacherName = new String();
			//Записываем занятия в определённый день
			for (int j = 0; j < ClassData.size(); j+=4)
			{
				matcher = classTimePattern.matcher(ClassData.get(j).text());
				if (matcher.find())
					classStartTime = matcher.group();
				else
					System.out.println("ERROR: Could not find class start time");

				if (matcher.find())
					classEndTime = matcher.group();
				else
					System.out.println("ERROR: Could not find class end time");

				className = ClassData.get(j+1).text();
				classAddress = ClassData.get(j+2).text();
				teacherName = ClassData.get(j+3).text();
				schedule.add(new DailySchedule(classDate, classStartTime, classEndTime, className, teacherName, classAddress));
			}


		}

		try {
			FileWriter calendarWriter = new FileWriter("calendar.ical");

			calendarWriter.write("BEGIN:VCALENDAR\n" +
					"VERSION:2.0\n" +
					"PRODID:-//ChSU//Zaharov Parser//\n" +
					"CALSCALE:GREGORIAN\n" +
					"METHOD:PUBLISH\n" +
					"X-WR-CALNAME:Календарь занятий\n" +
					"X-WR-TIMEZONE:Europe/Moscow\n" +
					"X-WR-CALDESC:Автоматически созданный календарь с расписанием занятий\n");

			LocalDateTime currentDateTime = LocalDateTime.now();
			DateTimeFormatter iCalFormat = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss", Locale.ENGLISH);
			DateTimeFormatter rawFormat = DateTimeFormatter.ofPattern("dd.MM.yyyyHH:mm");
			String currentDT = iCalFormat.format(currentDateTime);

			for (var i : schedule)
			{
				LocalDateTime startDT = LocalDateTime.parse(i.getClassDate() + i.getClassStartTime(), rawFormat);
				LocalDateTime endDT = LocalDateTime.parse(i.getClassDate() + i.getClassEndTime(), rawFormat);


				String dtstart = iCalFormat.format(startDT);
				String dtend = iCalFormat.format(endDT);;
				String dtstamp = currentDT;
				String uid = UUID.randomUUID().toString();
				String created = currentDT;
				String description = "Преподователь: " + i.getTeacherName();
				String lastModified = currentDT;
				String location = i.getClassAddress();
				String summary = i.getClassName();

				calendarWriter.write("BEGIN:VEVENT\n" +
						"DTSTART:" + dtstart + "\n" +
						"DTEND:" + dtend + "\n" +
						"DTSTAMP:"+ dtstamp + "\n" +
						"UID:" + uid + "\n" +
						"CREATED:" + created + "\n" +
						"DESCRIPTION:" + description + "\n" +
						"LAST-MODIFIED:" + lastModified + "\n" +
						"LOCATION:" + location + "\n" +
						"SEQUENCE:0\n" +
						"STATUS:CONFIRMED\n" +
						"SUMMARY:" + summary + "\n" +
						"TRANSP:OPAQUE\n" +
						"END:VEVENT\n");
			}


			calendarWriter.write("END:VCALENDAR");
			calendarWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}



	}
}