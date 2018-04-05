import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import models.*;

import java.io.*;
import java.util.*;

/**
 * Data Mining Project, Spring 2017
 * Created by Priyanka Desai
 * B-number: B00658664
 */
@SuppressWarnings("SpellCheckingInspection")
public class Main {

    private static final int PRODUCT_CATEGORIES = 15;
    private static final int TRAINING_NO_OF_DAYS = 118;
    private static final String FILE_BUYER_BASIC_INFO = "input-dataset/buyer_basic_info.txt";
    private static final String FILE_BUYER_HISTORICAL_QUANTITY = "input-dataset/buyer_historical_category15_quantity.txt";
    private static final String FILE_BUYER_HISTORICAL_MONEY = "input-dataset/buyer_historical_category15_money.txt";
    private static final String FILE_PRODUCT_FEATURES = "input-dataset/product_features.txt";
    private static final String FILE_KEY_PRODUCT_ID = "input-dataset/key_product_IDs.txt";
    private static final String FILE_TRADE_INFO_TRANINING = "input-dataset/trade_info_training.txt";
    private static final String FILE_PRODUCT_DISTRIBUTION_TRAINING_SET = "input-dataset/product_distribution_training_set.txt";
    private static final String OUTPUT_CSV_FILE = "intermediate_files/r_dataset.csv";
    private static final String FILE_PREDICTION = "prediction.csv";
    private static final String FILE_FINAL_OUTPUT = "final_output.txt";

    private static List<BuyerBasicInfo> buyerBasicInfos;
    private static List<BuyerHistoricalQuantity> buyerHistoricalQuantities;
    private static List<BuyerHistoricalMoney> buyerHistoricalAmount;
    private static List<ProductFeature> productFeatures;
    private static List<Integer> keyProductIds;
    private static List<TradeInfoTraining> tradeInfoTrainings;
    private static List<ProductDistributionTrainingSet> productDistributionTrainingSets;
    private static Map<Integer, Map<Integer, Integer>> buyerData;

    public static void main(String args[]) {
        if (args.length != 1) {
            System.err.println("Usage: Please specify option to run\n" +
                    "0 : Process data and generate intermediate file for R\n" +
                    "1 : Generate final output file");
            System.exit(1);
        }
        if (args[0].equals("0")) {
            readInputDataSet();
            filterKeyProductData();
            createHashMap();
            createCsvForR();
        } else if (args[0].equals("1")) {
            readKeyProductIds();
            createOutputFile();
        }
    }

    /**
     * Reads all files from the input dataset
     */
    private static void readInputDataSet() {
        readBuyerBasicInfo();
        readBuyerHistoricalQuantity();
        readBuyerHistoricalMoney();
        readProductFeatures();
        readKeyProductIds();
        readTradeInfoTraining();
        readProductDistributionTrainingSet();
    }

    /**
     * Filters the required data
     */
    private static void filterKeyProductData() {
        filterProductFeatures();
        filterTradeInfoTraining();
        filterProductDistributionTrainingSet();
    }

    /**
     * Creates a hashmap of the filtered data
     */
    private static void createHashMap() {
        buyerData = new HashMap<>();
        for (TradeInfoTraining tradeInfoTraining : tradeInfoTrainings) {
            int day = tradeInfoTraining.getTradeTime();
            int productId = tradeInfoTraining.getProductId();

            if (buyerData.containsKey(day)) {
                Map<Integer, Integer> productQuantityMap = buyerData.get(day);
                if (productQuantityMap.containsKey(productId)) {
                    productQuantityMap.put(productId, productQuantityMap.get(productId) + tradeInfoTraining.getTradeQuantity());
                } else {
                    productQuantityMap.put(productId, tradeInfoTraining.getTradeQuantity());
                }
            } else {
                Map<Integer, Integer> productQuantityMap = new HashMap<>();
                productQuantityMap.put(productId, tradeInfoTraining.getTradeQuantity());
                buyerData.put(day, productQuantityMap);
            }
        }
    }

    /**
     * Creates a .csv file which is provided as an input file to the Rscript
     */
    private static void createCsvForR() {
        CSVWriter csvWriter;
        try {
            csvWriter = new CSVWriter(new FileWriter(OUTPUT_CSV_FILE), ',');
            List<String[]> table = new ArrayList<>();
            String[] rowHeader = new String[102];
            rowHeader[0] = "Days";
            for (int i = 0; i < keyProductIds.size(); i++) {
                int productId = keyProductIds.get(i);
                rowHeader[i + 1] = "key_product_" + productId;
            }
            rowHeader[101] = "Overall Sales Quantity";
            table.add(rowHeader);

            Set<Integer> days = buyerData.keySet();
            for (int day : days) {
                Map<Integer, Integer> productQuantity = buyerData.get(day);
                Set<Integer> products = productQuantity.keySet();
                String[] row = new String[102];
                Arrays.fill(row, "0");
                row[0] = String.valueOf(day);
                int overallSaleQuantity = 0;
                for (int productId : products) {
                    int quantity = productQuantity.get(productId);
                    int keyProductIndex = keyProductIds.indexOf(productId);
                    row[keyProductIndex + 1] = String.valueOf(quantity);
                    overallSaleQuantity += quantity;
                }
                row[101] = String.valueOf(overallSaleQuantity);
                table.add(row);
            }

            csvWriter.writeAll(table);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a final output file, final.txt
     */
    private static void createOutputFile() {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(FILE_PREDICTION), ',');
            List<String[]> data = csvReader.readAll();
            String[][] newData = new String[101][30];

            int x, y = 0;
            for (String[] dataRow : data) {
                x = 1;
                for (int j = 1; j < dataRow.length; j++) {
                    if (y == 0) {
                        if (x == 1) {
                            newData[x - 1][y] = "0";
                        } else {
                            newData[x - 1][y] = String.valueOf(keyProductIds.get(x - 2));
                        }
                    } else if (j == 101) {
                        newData[0][y] = dataRow[j];
                    } else {
                        newData[x][y] = dataRow[j];
                    }
                    x++;
                }
                y++;
            }
            csvReader.close();

            CSVWriter csvWriter = new CSVWriter(new FileWriter(FILE_FINAL_OUTPUT), ' ', CSVWriter.NO_QUOTE_CHARACTER);
            for (String[] row : newData) {
                csvWriter.writeNext(row);
            }
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters the list productFeatures as per requirements
     */
    private static void filterProductFeatures() {
        productFeatures.removeIf(productFeature -> !keyProductIds.contains(productFeature.getProductId()));
    }

    /**
     * Filters the list tradeInfoTraining as per the requirements
     */
    private static void filterTradeInfoTraining() {
        tradeInfoTrainings.removeIf(tradeInfoTraining -> !keyProductIds.contains(tradeInfoTraining.getProductId()));
    }

    /**
     * Filters the list productDistributionTrainingSets as per requirements
     */
    private static void filterProductDistributionTrainingSet() {
        productDistributionTrainingSets.removeIf(productDistributionTrainingSet -> !keyProductIds.contains(productDistributionTrainingSet.getProductId()));
    }

    /**
     * Reads the file buyer_basic_info.txt from the input dataset
     */
    private static void readBuyerBasicInfo() {
        buyerBasicInfos = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_BUYER_BASIC_INFO));
            while (scanner.hasNext()) {
                BuyerBasicInfo buyerBasicInfo = new BuyerBasicInfo();
                buyerBasicInfo.setBuyerId(scanner.nextInt());
                buyerBasicInfo.setRegistrationTime(scanner.nextLong());
                buyerBasicInfo.setSellerLevel(scanner.nextInt());
                buyerBasicInfo.setBuyerLevel(scanner.nextInt());
                buyerBasicInfo.setAge(scanner.nextInt());
                buyerBasicInfo.setGender(scanner.nextInt());
                buyerBasicInfos.add(buyerBasicInfo);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file buyer_historical_category15_quantity.txt from the input dataset
     */
    private static void readBuyerHistoricalQuantity() {
        buyerHistoricalQuantities = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_BUYER_HISTORICAL_QUANTITY));
            while (scanner.hasNext()) {
                BuyerHistoricalQuantity buyerHistoricalQuantity = new BuyerHistoricalQuantity();
                buyerHistoricalQuantity.setBuyerId(scanner.nextInt());
                int[] consumptionQuantity = new int[PRODUCT_CATEGORIES];
                for (int i = 0; i < PRODUCT_CATEGORIES; i++) {
                    consumptionQuantity[i] = scanner.nextInt();
                }
                buyerHistoricalQuantity.setConsumption(consumptionQuantity);
                buyerHistoricalQuantities.add(buyerHistoricalQuantity);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reads the file buyer_historical_category15_money.txt from the input dataset
     */
    private static void readBuyerHistoricalMoney() {
        buyerHistoricalAmount = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_BUYER_HISTORICAL_MONEY));
            while (scanner.hasNext()) {
                BuyerHistoricalMoney buyerHistoricalMoney = new BuyerHistoricalMoney();
                buyerHistoricalMoney.setBuyerId(scanner.nextInt());
                double[] consumptionAmount = new double[PRODUCT_CATEGORIES];
                for (int i = 0; i < PRODUCT_CATEGORIES; i++) {
                    consumptionAmount[i] = scanner.nextDouble();
                }
                buyerHistoricalMoney.setConsumptionAmount(consumptionAmount);
                buyerHistoricalAmount.add(buyerHistoricalMoney);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file product_features.txt from the input dataset
     */
    private static void readProductFeatures() {
        productFeatures = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_PRODUCT_FEATURES));
            while (scanner.hasNext()) {
                ProductFeature productFeature = new ProductFeature();
                productFeature.setProductId(scanner.nextInt());
                productFeature.setAttribute_1(scanner.nextInt());
                productFeature.setAttribute_2(scanner.nextInt());
                productFeature.setOriginalPrice(scanner.nextDouble());
                productFeatures.add(productFeature);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file key_product_IDs.txt from the input dataset
     */
    private static void readKeyProductIds() {
        keyProductIds = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_KEY_PRODUCT_ID));
            while (scanner.hasNext()) {
                keyProductIds.add(scanner.nextInt());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file trade_info_training.txt from the input dataset
     */
    private static void readTradeInfoTraining() {
        tradeInfoTrainings = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_TRADE_INFO_TRANINING));
            while (scanner.hasNext()) {
                TradeInfoTraining tradeInfoTraining = new TradeInfoTraining();
                tradeInfoTraining.setProductId(scanner.nextInt());
                tradeInfoTraining.setBuyerId(scanner.nextInt());
                tradeInfoTraining.setTradeTime(scanner.nextInt());
                tradeInfoTraining.setTradeQuantity(scanner.nextInt());
                tradeInfoTraining.setTradePrice(scanner.nextDouble());
                tradeInfoTrainings.add(tradeInfoTraining);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file product_distribution_training_set.txt from the input dataset
     */
    private static void readProductDistributionTrainingSet() {
        productDistributionTrainingSets = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_PRODUCT_DISTRIBUTION_TRAINING_SET));
            while (scanner.hasNext()) {
                ProductDistributionTrainingSet productDistributionTrainingSet = new ProductDistributionTrainingSet();
                productDistributionTrainingSet.setProductId(scanner.nextInt());
                int[] quantitiesOfKeyProduct = new int[TRAINING_NO_OF_DAYS];
                for (int i = 0; i < TRAINING_NO_OF_DAYS; i++) {
                    quantitiesOfKeyProduct[i] = scanner.nextInt();
                }
                productDistributionTrainingSet.setQuantitiesOfKeyProduct(quantitiesOfKeyProduct);
                productDistributionTrainingSets.add(productDistributionTrainingSet);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
