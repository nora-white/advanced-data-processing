<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="cpsc4310" %>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        
        <title>CPSC4310 | Final Project | Web Scraper by Nora White</title>
        
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" type="text/css" href="css/styles.css"/>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    </head>
    
    <body>
        <div id="hero">
            <div id="content">
                <h1>Product search</h1>
                
                <div id="description">
                    Provide a brand and product to search Shoppers Drug Mart for.<br/>If it's found, the product's information will be displayed.
                </div>
                
                <form>
                    <div class="form-group row">
                        <label for="inputBrand" class="col-sm-2 col-form-label">Brand</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="inputBrand" placeholder="Brand">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputProduct" class="col-sm-2 col-form-label">Product name</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="inputProduct" placeholder="Product name">
                        </div>
                    </div>
                    <div class="form-group row center-button">
                        <button class="btn btn-primary" type="submit">Search</button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js, then Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </body>
</html>