<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Category;
use App\Models\Product;
use Exception;
use Illuminate\Http\Request;

class CategoryController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $categories = Category::all();
        return view('category.index', compact('categories'));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        return view('category.form');
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        // dd($request->all());
        if($request->hasFile('photo')){
            $filename = $request->file('photo')->getClientOriginalName();
           $path= $request->file('photo')->storeAs('categories', $filename,'public');
        }
        $categories = new Category();
        $categories->name = $request->name;
        $categories->photo = $path;
        $categories->save();

        return redirect('admin/category')->with('success','category added successfully');
    }
    

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $category = Category::findOrFail($id);
        return view('category.show', compact('category'));
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $category = Category::findOrFail($id);
        return view('category.update', compact('category'));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $category = Category::findOrFail($id);
        $path = $category->photo; // Preserve existing photo path if no new photo is uploaded

        try {
            if ($request->hasFile('photo')) {
                // Remove old photo if it exists
                if (file_exists(storage_path('app/public/' . $category->photo))) {
                    @unlink(storage_path('app/public/' . $category->photo));
                }

                // Handle file upload
                $filename = $request->file('photo')->getClientOriginalName();
                $path = $request->file('photo')->storeAs('categories', $filename, 'public');
            }

            // Update category details
            $category->name = $request->name;
            $category->photo = $path;
            $category->save();

            return redirect()->route('category.index')->with('success', 'Category updated successfully');
        } catch (Exception $ex) {
            return redirect()->route('category.index')->with('error', 'Unable to update category: ' . $ex->getMessage());
        }
    }


    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $category = Category::findOrFail($id); //select * from product where id= ... vanni ho hai yo code ko meaning
        Product::where('category_id', $id)->delete();

        if(file_exists(storage_path().'/app/public/'.$category->photo)){
            @unlink(storage_path().'/app/public/'.$category->photo);
        }
        $category->delete();
        return redirect('admin/category')->with('success','category deleted successfully');
    }
}
