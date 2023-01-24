package net.youssfi.springbatchcase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException {
        String strDate="01-01-2000";
        //LocalDate localDate=LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //Date date=Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Date date=simpleDateFormat.parse(strDate);

        LocalDate localDate=date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        System.out.println(date.toInstant());
        System.out.println(date.toInstant());
        System.out.println("-----------");
        System.out.println(String.format("%04d",localDate.getYear()));
        System.out.println(String.format("%02d",localDate.getMonthValue()));
        System.out.println(String.format("%02d",localDate.getDayOfMonth()));
        System.out.println(localDate.toString());
    }
}
