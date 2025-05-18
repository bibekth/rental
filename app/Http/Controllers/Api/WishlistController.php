<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Wishlist;
use Illuminate\Http\Request;

class WishlistController extends Controller
{
    public function store(Request $request)
    {
        $request->validate([
            'product_id' => 'required|exists:products,id',
        ]);

        $wishlist = Wishlist::updateOrCreate(
            [
                'user_id' => auth()->id(),
                'product_id' => $request->product_id
            ]
        );

        return response()->json(['message' => 'Product added to wishlist', 'data' => $wishlist], 201);
    }

    
    public function index()
    {
        $wishlists = Wishlist::with('product')->where('user_id', auth()->id())->get();

        return response()->json($wishlists);
    }

    
    public function destroy($productId)
    {
        $wishlist = Wishlist::where('user_id', auth()->id())->where('product_id', $productId)->firstOrFail();
                            
        $wishlist->delete();

        return response()->json(['message' => 'Product removed from wishlist']);
    }
}
