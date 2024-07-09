# Product-Management-System
This project is a Java-based Product Management System designed to streamline the process of managing and selling products. It consists of two main modules: Product Entry and Product Selling. The system uses TIBCO Jaspersoft Studio for generating reports and MySQL as the database for storing and managing data.



## Overview

The Product Management System is a Java-based application designed to streamline the process of managing and selling products. It includes two main modules: Product Entry and Product Selling. The system uses TIBCO Jaspersoft Studio for generating reports and MySQL as the database for storing and managing data.

## Features

### Product Entry Module
- **Add New Products**: Input fields for ID, Name, Category, Price, Stock, and Alert.
- **Manage Products**:
  - **New**: Add a new product entry.
  - **Save**: Save product information.
  - **Search**: Search existing products.
  - **Update**: Update product details.
  - **Delete**: Delete a product entry.
- **Product List**: Displays existing products in a table format with ID, Name, Category, Price, Stock, and Alert.

### Product Selling Module
- **Product Details**: 
  - **Product**: Dropdown to select a product.
  - **Quantity**: Enter the quantity of the product.
  - **Unit Price**: Display the unit price of the selected product.
  - **Discount**: Enter any applicable discount.
  - **Total**: Calculate the total price.
- **Cashier Information**: Field to enter the cashier's name.
- **Transaction Management**:
  - **Add**: Add products to the selling list.
  - **Remove**: Remove products from the selling list.
  - **Print**: Print the bill.
  - **New Customer**: Add new customer details.
- **Selling List**: Displays products being sold in a table format with No, Product, Quantity, Price, Discount, Total, and Product ID.
- **Transaction Summary**:
  - **Sub Total**: Display the subtotal of the transaction.
  - **Discount**: Display the discount applied.
  - **Cash Paid**: Enter the cash paid by the customer.
  - **Customer**: Enter customer details.
  - **Net Total**: Display the net total after discount.
  - **Total**: Display the total amount.
  - **Balance**: Display the balance amount.

## Database

The system uses MySQL to store and manage product and sales data. This ensures data persistence and enables complex queries for generating reports and analytics.

## Reports

The system utilizes TIBCO Jaspersoft Studio for generating comprehensive reports. 

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/ammar-misdhar/Product-Management-System.git
   ```

2. **Setup MySQL Database**:
   - Create a database named `product_management`.
   - Import the provided SQL script to create necessary tables.

3. **Configure Database Connection**:
   - Update the database connection details in the `config` file.

4. **Build and Run**:
   - Open the project in your preferred Java IDE (e.g., NetBeans).
   - Build and run the project.

## Usage

- **Product Entry**: Navigate to the Product Entry tab to add, update, search, and delete products.
- **Product Selling**: Navigate to the Product Selling tab to manage sales transactions.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss your ideas.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to reach out with any questions or feedback!
