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
        $purchaseDate = $request->purchase_date ? Carbon::createFromFormat('d-m-Y', $request->purchase_date)->format('Y-m-d') : null;
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

    public function gitlabWebhook(Request $request)
    {
        try {
            $secret = 'monkey@21';
            $gitlabToken = $request->header('X-Gitlab-Token');

            if ($gitlabToken !== $secret) {
                return $this->sendError('Invalid Token', null, 403);
                // return response()->json('Invalid token', 403);
            }

            $data = $request->all();

            if (isset($data['ref']) && $data['ref'] === 'refs/heads/main') {
                $output = [];
                $returnCode = 0;

                exec(`cd ~/public_html/rental && git pull origin main 2>&1`, $output, $returnCode);

                file_put_contents("gitlab_webhook.log", implode("\n", $output) . "\n", FILE_APPEND);
            }

            return response()->json(['success' => true, 'message' => 'success'], 200);
            // return response()->json('success', 200);
        } catch (Throwable $e) {
            return response()->json(['success' => false, 'message' => 'internal error'], 500);
            // return response()->json($e->getMessage(), 500);
        }
    }
}
