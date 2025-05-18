<?php

use App\Http\Controllers\Admin\CategoryController;
use App\Http\Controllers\Admin\ProductController;
use App\Http\Controllers\Admin\CommentController;
use App\Http\Controllers\Admin\OrderController;
use App\Http\Controllers\Admin\OrderDetailController;
use App\Http\Controllers\Admin\UserController;
use App\Http\Controllers\HomeController;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\Auth;

Route::get('/', function () {
    return redirect(route('login'));
});

Auth::routes();
Route::middleware(['auth'])->prefix('admin')->group(function(){
// Route::middleware('auth')->prefix('admin')->group(function(){

    Route::resource('product', ProductController::class);
    Route::resource('category', CategoryController::class);
    // dashboard banauna
    Route::get('/dashboard', [HomeController::class, 'index'])->name('dashboard');

    // yo chai single wala lai hai, mathi ko chai sabbey ko lagi huncha but yo step lai chai create lai xutey, store lai xutey, delete lai xutey gari garna parthiyo
    // Route::post('/product',[ProductController::class.'store'])->name('product.store');

    // Route::post('upload-product','uploadProduct');
    Route::resource('comment', CommentController::class);

    // Orders routes
    Route::resource('orders', OrderController::class);

    // Order Details routes
    Route::resource('orderdetails', OrderDetailController::class);

    // User routes
    Route::resource('user', UserController::class);
});
