package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {

    GridPane loginPage;
    HBox headerBar;
    HBox footerBar;
    Button signInButton;
    Label welcomeLabel;
    VBox body;
    Customer loggedInCustomer;

    ProductList productList = new ProductList();
    VBox productPage;
    Button placeOrderButton = new Button("Place Order");// for cart page

    ObservableList<Product>itemsInCart = FXCollections.observableArrayList();

    public BorderPane creatContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        // root.getChildren().add(loginPage);
        root.setTop(headerBar);
       // root.setCenter(loginPage);
        body = new VBox();
        body.setPadding((new Insets(10)));
        body.setAlignment(Pos.CENTER);
        root.setCenter(body);
        productPage = productList.getAllProducts();
        body.getChildren().add(productPage);
        root.setBottom(footerBar);

        return root;
    }

    public UserInterface() {

        creatLoginPage();
        creatHeaderBar();
        createFooterBar();
    }

    private void creatLoginPage() {
        // creating text and field controls for username and password

        Text userNameText = new Text("User Name");
        Text passwordText = new Text("Password");

        TextField userName = new TextField("vishwagnavikassai@gmail.com");
        userName.setPromptText("Type your user name here");
        PasswordField password = new PasswordField();//enables users of a JavaFX application to enter password which can then be read by the application. The PasswordField control does not show the texted entered into it. Instead it shows a circle for each character entered.
        password.setText("abc123");
        password.setPromptText("Type your password here");
        Label messageLabel = new Label("Hi");


        Button loginButton = new Button("Login");

        loginPage = new GridPane();
        loginPage.setAlignment(Pos.CENTER);// place the components at the center of loginpage
        loginPage.setHgap(10);// add horizontal gap btw components
        loginPage.setVgap(10);
        loginPage.add(userNameText, 0, 0);// adding the controls to the login page (layout gird)
        loginPage.add(userName, 1, 0);//Username control
        //Password control
        loginPage.add(passwordText, 0, 1);
        loginPage.add(password, 1, 1);

        loginPage.add(messageLabel, 0, 2);
        loginPage.add(loginButton, 1, 2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = userName.getText();
                String pass = password.getText();
                Login login = new Login();
                loggedInCustomer = login.customerLogin(name,pass);
                if(loggedInCustomer!=null){
                    //messageDisplay.setText("Welcome" + loggedInCustomer.getName());--no use
                    messageLabel.setText("Welcom " + loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome-" +loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);// showing the msg  on header bar
                    body.getChildren().clear(); // directing to product page
                    body.getChildren().add(productPage);
                }
                else {
                    messageLabel.setText("LogIn Failed !! please give correct user name and password.");
                }
            }
        });

    }


    private void creatHeaderBar() {
        //creating home button with image and show it in the header bar
        Button homeButton = new Button();
        Image image = new Image("C:\\Users\\vishw\\IdeaProjects\\ECommerce\\src\\img.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);



        // creating text field for search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here");// adding Placeholder text in the searchbar field
        searchBar.setPrefWidth(280);// we can increase the width of text field by using this method
        // If we want, we can also use the same method to increase buttonsize.

        Button searchButton = new Button("Search");// creating search button

        signInButton = new Button("Sign In");// creating sign in button
        welcomeLabel = new Label();

        Button cartButton = new Button("Cart");// creating cart button



        Button orderButton = new Button("Orders");// creating order button


        headerBar = new HBox();
        headerBar.setPadding(new Insets(10));// gives 10px horizontal gap between components
        headerBar.setSpacing(10);
        headerBar.setAlignment(Pos.CENTER);
        headerBar.getChildren().addAll(homeButton,searchBar, searchButton,signInButton, cartButton,orderButton);

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            //event handeler for signin button
            @Override
            //on click of sign in button.. direct to login page
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(loginPage);
                headerBar.getChildren().remove(signInButton);
            }
        });


        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            // on click of cart button it should show items added to cart in new page
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage = productList.getProductsInCart(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);
            }
        });


        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //ghfh

                if(itemsInCart==null){
                    //please select the product first to place order
                    showDialog("please add some product in the cart to place order!");
                    return;
                }
                if(loggedInCustomer == null) // if user try to place order without logging in ...send an alert to login first
                {
                    showDialog("Please login first to place order");
                    return;
                }
                int  count = Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count!=0) {
                    showDialog("Order for "+count+" products placed Successfully !!");
                }
                else {
                    showDialog("Order failed !!");
                }
            }
        });




        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);

                //if no used logged in and signin button is not present on header then only add signin button to header bar
               if(loggedInCustomer == null && headerBar.getChildren().indexOf(signInButton) == -1)
                 headerBar.getChildren().add(signInButton);
            }
        });
    }




    private void createFooterBar() {
        Button buyNowButton = new Button("Buy Now");
        Button addToCartButton = new Button("Add to Cart");

        footerBar = new HBox();
        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
        footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(buyNowButton,addToCartButton);


        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product==null){
                    //please select the product first to place order
                    showDialog("Please  Select a product first to place order !");
                    return;
                }
                if(loggedInCustomer == null) // if user try to place order without logging in ...send an alert to login first
                {
                    showDialog("Please login first to place order");
                    return;
                }
                boolean status = Order.placeOrder(loggedInCustomer,product);
                if(status) {
                    showDialog("Order Placed Successfully !!");
                }
                else {
                    showDialog("Order failed !!");
                }
            }
        });
        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product==null){
                    showDialog("Please select a product first to add it to Cart!");
                    return;
                }
                itemsInCart.add(product);
                showDialog("Selected item added to the cart successfully !");
            }
        });

    }


    private void showDialog(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("Message");
        alert.showAndWait();
    }
}