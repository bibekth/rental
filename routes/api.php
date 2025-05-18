<?php

use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\HomeController;
use App\Http\Controllers\Api\CommentController;
use App\Http\Controllers\Api\OrderController;
use App\Http\Controllers\Api\OrderDetailController;
use App\Http\Controllers\Api\WishlistController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

// there may be multiple route so group matra banako ho tyo
Route::controller(AuthController::class)->group(function(){
    // register ani comma functions ko name
    Route::post('register','AddRegister');
    // auth controller bhitra login khojcha
    Route::post('login','login');
    Route::post('verify','verifiedOtp');
    Route::post('resend','resendOtp');
    Route::post('forgot-password', 'forgotPassword');
    Route::post('resetpassword', 'resetPassword');
    // Route::post('update-profile','updateProfile');
    Route::post('logout','logout')->middleware('auth:sanctum');

});
// login garey si auni vaye chai yesto rakhna parcha
// Route::controller(HomeController::class)->middleware('auth:sanctum', 'role:user|admin')->group(function(){
// Route::controller(HomeController::class)->middleware('auth:sanctum')->group(function(){
    // natra yo
Route::controller(HomeController::class)->group(function(){
    Route::get('products', 'productList');
    Route::get('categories', 'categoryList');
    Route::post('upload-product','uploadProduct');
    Route::get('show-single-product/{id}','showSingleProduct');
});

Route::controller(CommentController::class)->middleware('auth:sanctum')->group(function () {
    Route::post('post-comment', 'store');
    Route::get('show-comments/{product_id}', 'index');
    Route::post('update-comment/{id}', 'update');
    Route::delete('delete-comment/{id}', 'destroy');
});

Route::controller(OrderController::class)->middleware('auth:sanctum')->group(function () {
    Route::post('place-order', 'store');
    Route::get('show-orders', 'index');
    Route::post('update-order/{id}', 'update');
    Route::delete('delete-order/{id}', 'destroy');
});

Route::controller(OrderDetailController::class)->middleware('auth:sanctum')->group(function () {
    Route::post('order-details-store', 'store');
    Route::get('order-details-show/{id}', 'show');
    Route::post('order-details-update/{id}', 'update');
    Route::delete('order-details-destroy/{id}', 'destroy');
});

Route::controller(WishlistController::class)->middleware('auth:sanctum')->group(function () {
    Route::post('wishlist-store', 'store');
    Route::get('wishlist-show', 'index');
    Route::delete('wishlist-delete/{productId}', 'destroy');
});


Route::controller(OrderController::class)->middleware('auth:sanctum')->group(function () {
    Route::post('create-orders', 'createOrder');
    // Route::get('show-orders/{id}', 'showOrder');

    // Route::get('orders/{id}', 'getOrderDetails');
    // Route::patch('orders/{id}','updateOrderStatus');
});
// Route::middleware('auth:sanctum')->group(function () {

    // Route::middleware('auth:sanctum')->group(function () {
    //     Route::get('orders', [OrderController::class, 'index']);
    //     Route::post('orders', [OrderController::class, 'store']);
    //     Route::get('orders/{id}', [OrderController::class, 'show']);
    //     Route::put('orders/{id}', [OrderController::class, 'update']);
    //     Route::delete('orders/{id}', [OrderController::class, 'destroy']);

    //     Route::get('orderdetails', [OrderDetailController::class, 'index']);
    //     Route::post('orderdetails', [OrderDetailController::class, 'store']);
    //     Route::get('orderdetails/{id}', [OrderDetailController::class, 'show']);
    //     Route::put('orderdetails/{id}', [OrderDetailController::class, 'update']);
    //     Route::delete('orderdetails/{id}', [OrderDetailController::class, 'destroy']);
    // });

Route::post('/github-webhooks', [HomeController::class, 'githubWebhook']);
