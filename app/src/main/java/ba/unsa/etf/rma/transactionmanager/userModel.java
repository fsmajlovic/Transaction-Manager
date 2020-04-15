package ba.unsa.etf.rma.transactionmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class userModel {
    public static Account account = new Account(42000.0, 35000.0, 5000.0);
    public static ArrayList<Transaction> transactions;

    static {
        try {
            transactions = fill();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Transaction> fill() throws ParseException{
        ArrayList<Transaction> transactions = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        transactions.add( new Transaction(sdf.parse("16-06-2020"), 100,
                "Electricity expenses", Transaction.Type.REGULARPAYMENT, "Standard monthly" +
                " bill for electricity expenses in January is $100. This amount goes to your power company. " +
                "Total amount of $100 has been included in your Transaction Manager app.",
                30, sdf.parse("15-08-2020")));
        transactions.add( new Transaction(sdf.parse("17-03-2020"), 4125,
                "Motorbike purchase", Transaction.Type.PURCHASE, "New Yamaha motorbike " +
                "bought at Yamaha Sarajevo motorbike store. Black color with the maximum speed of 220 km/h." +
                "Total amount of $4125 has been included in your Transaction Manager app.",
                0, null));
        transactions.add( new Transaction(sdf.parse("10-02-2020"), 2500,
                "February Salary", Transaction.Type.REGULARINCOME, "Monthly salary income " +
                "from AppDev CO. Total amount of $3500 has been included in your Transaction Manager app. ",
                30, sdf.parse("10-03-2020")));
        transactions.add( new Transaction(sdf.parse("15-03-2020"), 600,
                "Freelancer project", Transaction.Type.INDIVIDUALINCOME,
                "Paycheck for finishing several tasks for your Freelancer client: App " +
                        "development, SQL database established, MD5 Hashing added to profile passwords. " +
                        "Total amount of $600 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("18-05-2020"), 500,
                "Speeding ticket", Transaction.Type.INDIVIDUALPAYMENT,
                "Camera caught you violating the speed limit law. You have been speeding " +
                        "with the speed of 100 km/h in the 70 km/h area. Destination: \"Zmaja od Bosne, Sarajevo\" " +
                        "Total amount of $500 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("11-02-2020"), 150,
                "MS Office", Transaction.Type.REGULARPAYMENT,
                "Yearly Microsoft office subscription that allows you to use all of the " +
                        "office features and programs. All of the updates have been included as well. " +
                        "Total amount of $150 has been included in your Transaction Manager app.",
                365, sdf.parse("10-02-2021")));
        transactions.add( new Transaction(sdf.parse("17-02-2020"), 700,
                "ASUS monitor", Transaction.Type.PURCHASE, "New ASUS computer " +
                "monitor purchase. ASUS ROG Swift PG348Q 34\" Curved Ultra-wide G-SYNC Gaming Monitor " +
                "Total amount of 700 has been included in your Transaction Manager app.",
                0, null));
        transactions.add( new Transaction(sdf.parse("10-03-2020"), 2500,
                "March Salary", Transaction.Type.REGULARINCOME, "Monthly salary income " +
                "from AppDev CO. Total amount of $3500 has been included in your Transaction Manager app. ",
                30, sdf.parse("09-04-2020")));
        transactions.add( new Transaction(sdf.parse("01-04-2020"), 100,
                "Airbnb host", Transaction.Type.INDIVIDUALINCOME,
                "Luxury double room near center of Sarajevo" +
                        " including private bathroom and a shared kitchen has been rented for 4 days by you. " +
                        "Total amount of $100 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("18-03-2020"), 40,
                "Drone shipment", Transaction.Type.INDIVIDUALPAYMENT,
                "You have ordered a new drone via Ebay from China. Takeoff Weight: " +
                        "249 g, Diagonal Distance: 213 mm, Max Flight Time 30 minutes (measured while " +
                        "flying at 14 kph in windless conditions). You have to pay additional $40 for shipment. " +
                        "Total amount of $40 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("22-03-2020"), 30,
                "Water expenses", Transaction.Type.REGULARPAYMENT, "Standard monthly" +
                " bill for water expenses in is $30. This amount goes to your water company. " +
                "Total amount of $30 has been included in your Transaction Manager app.",
                30, sdf.parse("21-04-2020")));
        transactions.add( new Transaction(sdf.parse("17-02-2020"), 650,
                "Playstation purchase", Transaction.Type.PURCHASE, "New Playstatin 4" +
                "bought at Target Sarajevo videogame store. Specs: CPU : x86-64 AMD “Jaguar”, 8 cores, " +
                "Storage size: 500GB, 1TB. Total amount of $650 has been included in your Transaction Manager app.",
                0, null));
        transactions.add( new Transaction(sdf.parse("10-01-2020"), 2500,
                "January Salary", Transaction.Type.REGULARINCOME, "Monthly salary income " +
                "from AppDev CO. Total amount of $3500 has been included in your Transaction Manager app. ",
                30, sdf.parse("09-02-2020")));
        transactions.add( new Transaction(sdf.parse("11-05-2020"), 200,
                "Youtube income", Transaction.Type.INDIVIDUALINCOME,
                "Paycheck from youtube for your \"Visual Studio Tutorials\" profile." +
                        " Total amount of $200 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("01-06-2020"), 360,
                "Boarding ticket", Transaction.Type.INDIVIDUALPAYMENT,
                "Boarding ticket Sarajevo(SJJ) - Florenta(FLR) booked for 05.06.2020 date and " +
                        "return date 15.06.2020. Total amount of $360 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("15-05-2020"), 40,
                "Internet expenses", Transaction.Type.REGULARPAYMENT,
                "Bill for internet expenses is $40. This amount goes to BHTelecom internet provider. " +
                        "Total amount of $40 has been included in your Transaction Manager app.",
                30, sdf.parse("27-07-2020")));
        transactions.add( new Transaction(sdf.parse("01-09-2020"), 140,
                "RHCP tickets", Transaction.Type.PURCHASE, "Two tickets for the Red Hot Chili Peppers " +
                "performance in Firenze. Date: June, 13th. Total amount of $140 has been included in your Transaction Manager app.",
                0, null));
        transactions.add( new Transaction(sdf.parse("10-04-2020"), 2500,
                "April Salary", Transaction.Type.REGULARINCOME, "Monthly salary income " +
                "from AppDev CO. Total amount of $3500 has been included in your Transaction Manager app.",
                30, sdf.parse("09-05-2020")));
        transactions.add( new Transaction(sdf.parse("12-04-2020"), 1380,
                "Samsung Galaxy Z Flip", Transaction.Type.PURCHASE, "New Samsung Galaxy mobile phone. " +
                "Inovative Foldable Dynamic AMOLED capacitive touchscreen, 16M colors. " +
                "Total amount of $1380 has been included in your Transaction Manager app.",
                0, null));
        transactions.add( new Transaction(sdf.parse("07-02-2020"), 1200,
                "Fender Stratocaster", Transaction.Type.PURCHASE, "New Fender Custom 57 Stratocaster 3" +
                " Color Sunburst with a Marshal studio amp. Total amount of $1200 has been included in your Transaction Manager app.",
                0, null));

        transactions.add( new Transaction(sdf.parse("02-06-2020"), 300,
                "Regular P 1", Transaction.Type.REGULARPAYMENT, "Regular payment 1",
                20, sdf.parse("22-10-2020")));

        transactions.add( new Transaction(sdf.parse("22-04-2020"), 1110,
                "Bonus Salary 1", Transaction.Type.REGULARINCOME, "This is your Freelancer Bonus Salary that you receive " +
                "for every month from April to July throughout the year.",
                30, sdf.parse("22-07-2020")));

        transactions.add( new Transaction(sdf.parse("22-07-2020"), 600,
                "Producer Income", Transaction.Type.REGULARINCOME, "This is your Producer income that you will receive" +
                "for every month from July to December throughout the year.",
                30, sdf.parse("22-12-2020")));

        transactions.add( new Transaction(sdf.parse("22-10-2020"), 550,
                "Bonus Income", Transaction.Type.REGULARINCOME, "This is your Bonus income that you will receive" +
                "for every month from October to December throughout the year.",
                30, sdf.parse("31-12-2020")));

        transactions.add( new Transaction(sdf.parse("02-01-2020"), 370,
                "Monthly Salary Bonus", Transaction.Type.REGULARINCOME, "Every month throughout the year you receive this bonus",
                30, sdf.parse("31-12-2020")));

        transactions.add( new Transaction(sdf.parse("03-01-2020"), 550,
                "Yearly Expenses 1", Transaction.Type.REGULARPAYMENT, "Every month throughout the year you pay this expenses",
                30, sdf.parse("30-12-2020")));

        transactions.add( new Transaction(sdf.parse("03-01-2020"), 550,
                "Yearly Expenses 2", Transaction.Type.REGULARPAYMENT, "Additional yearly expenses. You pay this amount every month.",
                30, sdf.parse("30-12-2020")));

        transactions.add( new Transaction(sdf.parse("01-01-2020"), 10,
                "Daily donations", Transaction.Type.REGULARINCOME, "Daily donations from adds you have implemented on various websites.",
                1, sdf.parse("30-12-2020")));


        return transactions;
    }

    public userModel(Account account, ArrayList<Transaction> transactions) {
        this.account = account;
        this.transactions = transactions;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
