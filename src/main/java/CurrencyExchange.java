import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrencyExchange {
    public static void main(String[] args){

        List<String> currency = new ArrayList<>();
        List<String> exRate = new ArrayList<>();
        currenciesLoader(currency, exRate);

        JFrame win = new JFrame("Currency Exchange");
            win.setBounds(100,100, 350,135);
            win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            win.setResizable(false);
            win.setIconImage(new ImageIcon("src/main/resources/dollar.png").getImage());

        JComboBox<String> currenciesList = new JComboBox<>();
            currenciesList.setBounds(5, 35, 180, 25);

        JComboBox<String> currenciesList2 = new JComboBox<>();
            currenciesList2.setBounds(5, 65, 180, 25);

        for (String a:
             currency) {
            currenciesList.addItem(a.substring(a.indexOf(" ")+1));
            currenciesList2.addItem(a.substring(a.indexOf(" ")+1));
        }

        currenciesList.setSelectedItem("Polish Złoty");
        currenciesList2.setSelectedItem("US Dollar");

        JTextField ammout = new JTextField("1");
            ammout.setBounds(5, 5, 180, 25);

        JLabel result = new JLabel("1.000");
            result.setForeground(Color.white);
            result.setBounds(250, 35, 60,20);

        currenciesList.addActionListener(e -> {

            System.out.println(rateFinder(Objects.requireNonNull(currenciesList.getSelectedItem()).toString(), currency, exRate));

            convert(Float.parseFloat(ammout.getText()), rateFinder(currenciesList.getSelectedItem().toString(), currency, exRate), rateFinder(Objects.requireNonNull(currenciesList2.getSelectedItem()).toString(), currency, exRate), result);

        });
        currenciesList2.addActionListener(e -> {

            System.out.println(rateFinder(Objects.requireNonNull(currenciesList.getSelectedItem()).toString(), currency, exRate));

            convert(Float.parseFloat(ammout.getText()), rateFinder(currenciesList.getSelectedItem().toString(), currency, exRate), rateFinder(Objects.requireNonNull(currenciesList2.getSelectedItem()).toString(), currency, exRate), result);

        });

        JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(Color.decode("#022181"));

        {
            panel.add(currenciesList);
            panel.add(currenciesList2);
            panel.add(result);
            panel.add(ammout);
            win.add(panel);
        }

        win.setVisible(true);

    }

    static void currenciesLoader(List<String> currency, List<String> exRate){

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://nbp.pl/en/statistic-and-financial-reporting/rates/table-a/");

        int i;

        for (i = 1; i < i+1; i++) {

            try {
                currency.add(i + " " + driver.findElement(By.xpath(String.format("/html/body/main/div/section/div/figure/table/tbody/tr[%s]/td[1]", i))).getText());
                exRate.add(i + " " + driver.findElement(By.xpath(String.format("/html/body/main/div/section/div/figure/table/tbody/tr[%s]/td[3]", i))).getText());
            } catch (Exception e) {
                break;
            }

        }

        driver.get("https://nbp.pl/en/statistic-and-financial-reporting/rates/table-b/");

        for (int j = 1; j < j+1; j++) {

            i++;

            try {
                currency.add(i + " " + driver.findElement(By.xpath(String.format("/html/body/main/div/section/div/figure/table/tbody/tr[%s]/td[1]", j))).getText());
                exRate.add(i + " " + driver.findElement(By.xpath(String.format("/html/body/main/div/section/div/figure/table/tbody/tr[%s]/td[3]", j))).getText());
            } catch (Exception e) {
                break;
            }

        }

        currency.add(i + " Polish Złoty");
        exRate.add(i + " 1");

        driver.close();

    }

    static float rateFinder(String text, List<String> currency, List<String> exRate){

        for (int i = 0; i < i+1; i++) {

            if (currency.get(i).contains(text)){
                return Float.parseFloat(exRate.get(i).substring(exRate.get(i).indexOf(" ")+1));
            }

        }

        return 1;

    }

    static void convert(float ammout, float currA, float currB, JLabel result){

        result.setText(String.format("%.3f", ammout * currA / currB));

    }

}

/*

    Title: Currency Exchange
    Description: Currency Exchange App based on NBP exchange rates
    Author: Maciej Sepeta

    External Libraries: Selenium, WebDriverManager
    Data source: https://nbp.pl/en/statistic-and-financial-reporting/rates/table-a/
                 https://nbp.pl/en/statistic-and-financial-reporting/rates/table-b/

 */