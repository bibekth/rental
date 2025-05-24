<?php

namespace App\Http\Controllers\Api;

use App\Helper\ResponseHelper;
use App\Http\Controllers\Controller;
use App\Http\Requests\ProductUploadRequest;
use App\Models\Category;
use App\Models\Product;
use Carbon\Carbon;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Validator;
use Throwable;

class HomeController extends Controller
{
    public function productList()
    {
        // yo vanya chai select * garya jasto ho hai
        // $products = Product::get();
        // $products = DB::table('product')
        $products = Product::with('category')->get();
        // $products = Product::with('category')-> take(1)->get();
        // $products = Product::with('category')->where('amount', 25)->take(1)->get();
        if ($products->isNotEmpty()) {
            return ResponseHelper::success(message: 'All Products', data: $products, statusCode: 200);
        } else {
            return ResponseHelper::success(message: 'No products found', data: [], statusCode: 200);
        }
    }
    
    public function myProductList()
    {
        // yo vanya chai select * garya jasto ho hai
        // $products = Product::get();
        // $products = DB::table('product')
        $products = Product::with('category')->where('user_id', Auth::id())->get();
        // $products = Product::with('category')-> take(1)->get();
        // $products = Product::with('category')->where('amount', 25)->take(1)->get();
        if ($products->isNotEmpty()) {
            return ResponseHelper::success(message: 'All Products', data: $products, statusCode: 200);
        } else {
            return ResponseHelper::success(message: 'No products found', data: [], statusCode: 200);
        }
    }

    public function categoryList()
    {
        $categories = Category::with('products')->get();
        if ($categories) {
            return ResponseHelper::success(message: 'All Category', data: $categories, statusCode: 200);
        } else {
            return ResponseHelper::success(message: 'No catagories found', data: [], statusCode: 200);
        }
    }

    public function uploadProduct(ProductUploadRequest $request)
    {
        if ($request->hasFile('photo')) {
            $filename = $request->file('photo')->getClientOriginalName();
            $path = $request->file('photo')->storeAs('products', $filename, 'public');
        }
        $purchaseDate = $request->purchase_date ? Carbon::parse($request->purchase_date)->format('Y-m-d') : null;
        try {
            $product = new Product();
            $product->name = $request->name;
            $product->description = $request->description;
            $product->amount = $request->amount;
            $product->photo = $path;
            $product->purchase_date = $purchaseDate;
            $product->category_id = $request->category_id;
            $product->save();

            return ResponseHelper::success(message: 'Product uploaded successfully', data: $product, statusCode: 201);
        } catch (Exception $ex) {
            // return response()->json(['message'=>'Unable to save: ' . $ex->getMessage()], 200);
            return ResponseHelper::errors(message: 'Unable to save: ' . $ex->getMessage(), statusCode: 500);
        }
    }

    public function showSingleProduct($id)
    {
        try {
            $product = Product::with('category')->findOrFail($id);
            return ResponseHelper::success(message: 'Product found', data: $product, statusCode: 200);
        } catch (Exception $ex) {
            return ResponseHelper::errors(message: 'Product not found: ' . $ex->getMessage(), statusCode: 404);
        }
    }

    public function githubWebhook(Request $request)
    {
        try {
            $secret = "monkey@21";
            $payload = file_get_contents("php://input");
            // file_put_contents("webhook_request.log", $payload, FILE_APPEND);
            $signature = $_SERVER["HTTP_X_HUB_SIGNATURE_256"] ?? "";
            $hash = "sha256=" . hash_hmac("sha256", $payload, $secret);
            if (!hash_equals($hash, $signature)) {
                http_response_code(403);
                exit("Invalid Signature");
            }

            $data = json_decode($payload, true);
            if ($data["ref"] === "refs/heads/main") {
                exec("cd ~/public_html/rental && git pull origin main 2>&1", $output, $returnCode);
                file_put_contents("webhook.log", implode('\n', $output), FILE_APPEND);
            }
            return response()->json('success', 200);
        } catch (Exception $e) {
            return response()->json($e->getMessage(), 500);
        }
    }
}
