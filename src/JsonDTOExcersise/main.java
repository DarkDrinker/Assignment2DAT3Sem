package JsonDTOExcersise;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class main {
    /*
    Answer questions
    1. JSON stand for  : JavaScript Object Notation
    2. JSON vs XML  : JSON dosent use end tag, is shorter, easier to read and write and can use arrays. Also XML have to be used with XML Parser, while JSON is a standard JavaScript Function
    3. JSON is usually used to storing data. Because Text is always a valid format when looking at datatypes
    4. a string, a number, an object (JSON object), an array, a boolean, null
    5. Key and value pair: "name": "John"
       JSON format is almost identical to JS Objects
       same as 4. values
       filetype is ".json" or MIME type is "application/json"
     */

    public static void main(String[] args) throws IOException, InterruptedException {
        main mdbee = new main();
        String filename = "src/JsonDTOExcersise/account.json";
        JsonTestDTO[] arrayofjsonobjects = mdbee.addJsonData(filename);

        for (JsonTestDTO accounts : arrayofjsonobjects){
            System.out.println("Full name: "+ accounts.fullName);
            //cant seem to properly output address and contents within arrays :/
            System.out.println();
        }

    }
    private JsonTestDTO[] addJsonData(String filename){
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(filename);
            JsonTestDTO[] accounts = gson.fromJson(reader, JsonTestDTO[].class);
            for (JsonTestDTO account : accounts){
                account.setFullName(account.firstName+" "+account.lastName);
            }
            return accounts;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class JsonTestDTO {
        private String fullName;
        private String firstName;
        private String lastName;
        private String birthDate;
        private List<address> addresses;
        private account Account;

    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    private static class address{
        private String street;
        private String city;
        private String zipCode;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    private static class account {
        private String id;
        private String balance;
        private boolean isActive;
    }
}
