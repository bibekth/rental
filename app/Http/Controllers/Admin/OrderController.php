<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\Product;
use App\Models\User;
use Illuminate\Http\Request;

class OrderController extends Controller
{
    public function index()
    {
        $orders = Order::with(['user', 'product'])->get();
        return view('orders.index', compact('orders'));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        $users = User::all();
        $products = Product::all();
        return view('orders.form', compact('users', 'products'));
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $request->validate([
            'user_id' => 'required|exists:users,id',
            'product_id' => 'required|exists:products,id',
            'order_date' => 'required|date',
            'status' => 'required|integer',
            'quantity' => 'nullable|integer',
            'offer' => 'nullable|string',
        ]);

        $order = new Order();
        $order->user_id = $request->user_id;
        $order->product_id = $request->product_id;
        $order->order_date = $request->order_date;
        $order->status = $request->status;
        $order->quantity = $request->quantity;
        $order->offer = $request->offer;
        $order->save();

        return redirect('admin/orders')->with('success', 'Order added successfully');
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $order = Order::with(['user', 'product'])->findOrFail($id);
        return view('orders.show', compact('order'));
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $order = Order::findOrFail($id);
        $users = User::all();
        $products = Product::all();
        return view('orders.update', compact('order', 'users', 'products'));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $request->validate([
            'user_id' => 'required|exists:users,id',
            'product_id' => 'required|exists:products,id',
            'order_date' => 'required|date',
            'status' => 'nullable|integer',
            'quantity' => 'nullable|integer',
            'offer' => 'nullable|string',
        ]);

        $order = Order::findOrFail($id);
        $order->user_id = $request->user_id;
        $order->product_id = $request->product_id;
        $order->order_date = $request->order_date;
        $order->status = $request->status;
        $order->quantity = $request->quantity;
        $order->offer = $request->offer;
        $order->save();

        return redirect('admin/orders')->with('success', 'Order updated successfully');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $order = Order::findOrFail($id);
        $order->delete();
        return redirect('admin/orders')->with('success', 'Order deleted successfully');
    }
}
