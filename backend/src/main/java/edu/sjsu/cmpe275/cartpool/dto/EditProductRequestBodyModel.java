package edu.sjsu.cmpe275.cartpool.dto;

public class EditProductRequestBodyModel {

        private String name;

        private String description;

        private String imageURL;

        private String brand;

        private double price;

        private String unit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }


        @Override
        public String toString() {
            return "EditProductRequestBodyModel{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", imageURL='" + imageURL + '\'' +
                    ", brand='" + brand + '\'' +
                    ", price=" + price +
                    ", unit='" + unit + '\'' +
                    '}';
        }

}

