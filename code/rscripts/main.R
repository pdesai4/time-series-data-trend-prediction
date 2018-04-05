library(astsa)

#Read .csv file into data
data <- read.csv("intermediate_files/r_dataset.csv")

#Remove the 'Days' column from the input file
drops <- c("Days")
data <- data[, ! (names(data) %in% drops)]

#Add data to the R search path
attach(data)

#Read key_product_IDs.txt file into keyProducts
keyProducts <- read.table("input-dataset/key_product_IDs.txt")

#Initialize a global variable resultAll
resultAll <<- NULL

#A function to perform prediction for all the passed values
arimaPrediction <- function(x) {
    print(x)

    #frequency is 7 since the data is sampled daily
    originalX <- ts(x, frequency = 7, start = 0)
    par(mfrow = c(2, 1))
    plot(originalX)
    #Making data stationary
    diffX <- diff(originalX)
    plot(diffX)

    #Plot the p/acf on the differenced data
    sampleAcfPcf <- acf2(diffX)
    print(sampleAcfPcf)

    #Fit all the ar and ma models
    model100 <- sarima(originalX, 1, 0, 0)
    model001 <- sarima(originalX, 0, 0, 1)
    model101 <- sarima(originalX, 1, 0, 1)

    model200 <- sarima(originalX, 2, 0, 0)
    model002 <- sarima(originalX, 0, 0, 2)

    model102 <- sarima(originalX, 1, 0, 2)
    model201 <- sarima(originalX, 2, 0, 1)
    model202 <- sarima(originalX, 2, 0, 2)

    #Examine the AIC and BIC of all the models
    print(c(model100$AIC, model100$BIC))
    print(c(model001$AIC, model001$BIC))
    print(c(model101$AIC, model101$BIC))

    print(c(model200$AIC, model200$BIC))
    print(c(model002$AIC, model002$BIC))

    print(c(model102$AIC, model102$BIC))
    print(c(model201$AIC, model201$BIC))
    print(c(model202$AIC, model202$BIC))

    #Construct a matrix of the AIC and BIC of all the models
    mat = matrix(data = c(model100$AIC, model100$BIC,
    model001$AIC, model001$BIC,
    model101$AIC, model101$BIC,
    model200$AIC, model200$BIC,
    model002$AIC, model002$BIC,
    model102$AIC, model102$BIC,
    model201$AIC, model201$BIC,
    model202$AIC, model202$BIC),
    nrow = 8, ncol = 2, byrow = TRUE)
    print(mat)

    #Pick the row with the least value from the matrix
    row = which(mat == min(mat), arr.ind = TRUE)[1, 1]
    print(row)

    #Select the appropriate arma model depending upon the row value
    if (row == 1) {
        result = sarima.for(originalX, 29, 1, 0, 0)
    } else if (row == 2) {
        result = sarima.for(originalX, 29, 0, 0, 1)
    } else if (row == 3) {
        result = sarima.for(originalX, 29, 1, 0, 1)
    } else if (row == 4) {
        result = sarima.for(originalX, 29, 2, 0, 0)
    } else if (row == 5) {
        result = sarima.for(originalX, 29, 0, 0, 2)
    } else if (row == 6) {
        result = sarima.for(originalX, 29, 1, 0, 2)
    } else if (row == 7) {
        result = sarima.for(originalX, 29, 2, 0, 1)
    } else {
        result = sarima.for(originalX, 29, 2, 0, 2)
    }

    #Creating a matrix, with the predicted values, by row keeping the number of columns as 1
    current <- matrix(c(result$pred), ncol = 1, byrow = TRUE)

    #Rounding off the predicted values
    current <- round(current, digits = 0)

    #Update the global variable with the result
    resultAll <<- cbind(resultAll, current)
}

#Apply the function arimaPrediction to all the columns original data in data
apply(data, 2, arimaPrediction)

#Name all the rows in the output file from 118 to 146 indicating the days
row.names(resultAll) <- c(118 : 146)

#Name all the columns in the output file
colnames(resultAll) <- c(paste0("key_product_", keyProducts[[1]]), "Overall Sales Quantity")
print(resultAll)

#Write the result into prediction.csv file
write.csv(resultAll, file = "prediction.csv")
