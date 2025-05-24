<?php

namespace App\Http\Controllers\Admin;

use App\Helper\ResponseHelper;
use App\Http\Controllers\Controller;
use App\Http\Requests\ProductUploadRequest;
use App\Models\Category;
use App\Models\Product;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class ProductController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        // $products = Product::all();
        // yo category chai hamle category sngha jlin gareko thiyo ni ali aasti ho tya bta aakoooo hai
        $products = Product::with('category')->get();
        return view('product.index', compact('products'));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        $categories = Category::all();
        return view('product.form', compact('categories'));

    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(ProductUploadRequest $request)
    {
        // dd($request->all());
        if($request->hasFile('photo')){
            $filename = $request->file('photo')->getClientOriginalName();
           $path= $request->file('photo')->storeAs('products', $filename,'public');
        }
            $product = new Product();
            $product->user_id = Auth::id();
            $product->name = $request->name;
            $product->description = $request->description;
            $product->amount = $request->amount;
            $product->photo = $path;
            $product->purchase_date = $request->purchase_date;
            $product->category_id = $request->category_id;
            $product->save();

            return redirect('admin/product')->with('success','product added successfully');
            }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $product = Product::with('category')->findOrFail($id);
        return view('product.show', compact('product'));
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $categories = Category::all();
        $product = Product::findOrFail($id);
        return view('product.update', compact('categories', 'product'));

    }

    /**
     * Update the specified resource in storage.
     */
    public function update(ProductUploadRequest $request, string $id)
    {
        $product = Product::findOrFail($id);

        if($request->hasFile('photo')){
            $filename = $request->file('photo')->getClientOriginalName();
            $path= $request->file('photo')->storeAs('products', $filename,'public');
            @unlink(storage_path().'/app/public/'.$product->photo);
            $product->photo = $path;
        }
        $product->name = $request->name;
        $product->description = $request->description;
        $product->amount = $request->amount;
        $product->purchase_date = $request->purchase_date;
        $product->category_id = $request->category_id;
        $product->save();
        return redirect('admin/product')->with('success','product updated successfully');
        
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        // dd($id);
        $product = Product::findOrFail($id); //select * from product where id= ... vanni ho hai yo code ko meaning
        if(file_exists(storage_path().'/app/public/'.$product->photo)){
            @unlink(storage_path().'/app/public/'.$product->photo);
        }
        $product->delete();
        return redirect('admin/product')->with('success','product deleted successfully');

    }
}
