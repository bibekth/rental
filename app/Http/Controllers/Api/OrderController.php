<?php

namespace App\Http\Controllers\Api;

use App\Helper\ResponseHelper;
use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\Product;
use Exception;
use Illuminate\Http\Request;

class OrderController extends Controller
{
    public function store(Request $request)
    {
        $request->validate([
            'product_id' => 'required|exists:products,id',
            'quantity' => 'nullable|integer|min:1',
            'order_date' => 'required|date',
            'status' => 'nullable|boolean',
            'offer' => 'nullable|string|max:255',
        ]);
        $product = Product::findOrFail($request->product_id);
        $existingOrder = Order::where('product_id', $request->product_id)
        ->where('status', false) // status = false (sold)
        ->first();

    if ($existingOrder) {
        return ResponseHelper::errors(message: 'Product is already sold.', statusCode: 400);
    }

        try {
            $order = new Order();
            $order->user_id = auth()->id();
            $order->product_id = $request->product_id;
            $order->quantity = $request->quantity;
            $order->order_date = $request->order_date;
            $order->status = true;
            $order->offer = $request->offer;
            $order->save();
            
            Order::where('product_id', $request->product_id)
            ->where('status', true) // Only update if it's currently available
            ->update(['status' => false]);

            return ResponseHelper::success(message: 'Order placed successfully!', data: $order, statusCode: 201);
        } catch (Exception $ex) {
            return ResponseHelper::errors(message: 'Unable to place order: ' . $ex->getMessage(), statusCode: 500);
        }
    }

    public function index()
    {
        $orders = Order::with(['user', 'product', 'orderDetails'])->latest()->get();

        if ($orders->isNotEmpty()) {
            return ResponseHelper::success(message: 'All Orders', data: $orders, statusCode: 200);
        } else {
            return ResponseHelper::success(message: 'No orders found', data: [], statusCode: 200);
        }
    }

    public function update(Request $request, $id)
    {
        $request->validate([
            'status' => 'nullable|boolean',
            'quantity' => 'nullable|integer|min:1',
        ]);

        try {
            $order = Order::findOrFail($id);
            $order->status = $request->status; // Update status if provided
            $order->quantity = $request->quantity;
            $order->save();

            return response()->json([
                'success' => true,
                'message' => 'Order updated successfully',
                'data' => $order,
            ], 200);
        } catch (Exception $ex) {
            return response()->json([
                'success' => false,
                'message' => 'Unable to update order: ' . $ex->getMessage(),
            ], 500);
            }
        }

    public function destroy($id)
    {
        try {
            $order = Order::findOrFail($id);
    
            // Check if the authenticated user is the owner of the order or an admin
            if (auth()->id() === $order->user_id || auth()->user()->role === 'admin') {
                // Update the order status to 'available' (or whatever logic you use)
                $order->status = true; // Assuming `true` means available
                $order->save();
    
                // Delete the order
                $order->delete();
    
                return ResponseHelper::success(message: 'Order deleted successfully', data: null, statusCode: 200);
            }
    
            return ResponseHelper::errors(message: 'Unauthorized', statusCode: 403);
        } catch (Exception $ex) {
            return ResponseHelper::errors(message: 'Unable to delete order: ' . $ex->getMessage(), statusCode: 500);
        }
    }
}
