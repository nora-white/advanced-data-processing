<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                <c:choose>
                    <c:when test="${searchCompleted}">
                        <h1 class="big-margin-top">Product search</h1>
                    </c:when>
                    <c:otherwise>
                        <h1>Product search</h1>
                    </c:otherwise>
                </c:choose>
                
                <div id="description">
                    Provide a brand and product to search Sephora for.<br/>If it's found, the product's information will be displayed.
                </div>
                
                <form class="form" method="post" action="index">
                    <div class="form-group row">
                        <label for="inputBrand" class="col-sm-2 col-form-label">Brand</label>
                        <div class="col-sm-10">
                            <input name="inputBrand" type="text" class="form-control" id="inputBrand" placeholder="Brand" value="${inputBrand}" />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputProduct" class="col-sm-2 col-form-label">Product name</label>
                        <div class="col-sm-10">
                            <input name="inputProduct" type="text" class="form-control" id="inputProduct" placeholder="Product name" value="${inputProduct}" />
                        </div>
                    </div>
                    <div class="form-group row center-button">
                        <input class="btn btn-primary" type="submit" value="Search">
                    </div>
                </form>
                
                <c:if test="${searchCompleted}">
                    <div id="result">

                        <!-- Crawler data -->
                        <div class="container">

                            <div class="row">
                                <div class="col-sm-12">
                                    <h2>Crawler results</h2>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-2 title">
                                    Crawl delay
                                </div>
                                <div class="col-sm-4">
                                    ${crawlDelay} seconds
                                </div>
                                <div class="col-sm-2 title">
                                    Sitemap
                                </div>
                                <div class="col-sm-4">
                                    <a href="${sitemapURL}">${sitemapURL}</a>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-2 title">
                                    DOM results
                                </div>
                                <div class="col-sm-4">
                                    ${domTime} with ${domTotalResults} product(s) found
                                </div>
                                <div class="col-sm-2 title">
                                    StAX results
                                </div>
                                <div class="col-sm-4">
                                    ${staxTime} with ${staxTotalResults} product(s) found
                                </div>
                            </div>

                        </div>

                        <!-- Result data -->
                        <div class="container margin-top">

                            <div class="row">
                                <div class="col-sm-12">
                                    <h2>Found product(s)</h2>
                                </div>
                            </div>

                            <c:forEach items="${products}" var="product">

                                <div class="row margin-top product-row">

                                    <!-- Image column -->
                                    <div class="col-sm-2">
                                       <img src="${product.imgurl}" class="img-fluid" />
                                    </div>

                                    <!-- Product data column -->
                                    <div class="col-sm-10">

                                        <div class="row">
                                            <div class="col-sm-12 product-brand">
                                                ${product.brand}
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-12 product-name">
                                                ${product.name}
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-2">
                                                $<fmt:formatNumber type="number" maxFractionDigits="2" value="${product.price}"/> CAD
                                            </div>
                                            <div class="col-sm-2">
                                                ${product.sizeoz} / ${product.sizeg}
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-12">
                                                <a href="${product.producturl}">${product.producturl}</a>
                                            </div>
                                        </div>
                                    </div>

                                </div>

                            </c:forEach>

                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        
        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js, then Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </body>
</html>