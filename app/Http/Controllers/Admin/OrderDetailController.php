<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\OrderDetails;
use Illuminate\Http\Request;

class OrderDetailController extends Controller
{
/**
     * Display a listing of the resource.
     */
    public function index()
    {
        $orderDetails = OrderDetails::with('order')->get();
        return view('orderdetails.index', compact('orderDetails'));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        $orders = Order::all();
        return view('orderdetails.form', compact('orders'));
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'order_id' => 'nullable|exists:orders,id',
            'discount' => 'nullable|numeric',
            'deliveryaddress' => 'nullable|string',
            'payment_method' => 'nullable|string',
            'statuspayment' => 'nullable|string',
            'mobilenumber' => 'nullable|string',
            'deliverystatus' => 'nullable|string',
        ]);

        $orderDetails = new OrderDetails();
        $orderDetails->order_id = $validated['order_id'];
        $orderDetails->discount = $validated['discount'];
        $orderDetails->deliveryaddress = $validated['deliveryaddress'];
        $orderDetails->payment_method = $validated['payment_method'];
        $orderDetails->statuspayment = $validated['statuspayment'];
        $orderDetails->mobilenumber = $validated['mobilenumber'];
        $orderDetails->deliverystatus = $validated['deliverystatus'];
        $orderDetails->save();

        return redirect('admin/orderdetails')->with('success', 'Order details added successfully');
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $orderDetails = OrderDetails::with('order')->findOrFail($id);
        return view('orderdetails.show', compact('orderDetails'));
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        $orders = Order::all();
        $orderDetails = OrderDetails::findOrFail($id);
        return view('orderdetails.update', compact('orders', 'orderDetails'));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        $validated = $request->validate([
            'order_id' => 'nullable|exists:orders,id',
            'discount' => 'nullable|numeric',
            'deliveryaddress' => 'nullable|string',
            'payment_method' => 'nullable|string',
            'statuspayment' => 'nullable|string',
            'mobilenumber' => 'nullable|string',
            'deliverystatus' => 'nullable|string',
        ]);

        $orderDetails = OrderDetails::findOrFail($id);
        $orderDetails->order_id = $validated['order_id'];
        $orderDetails->discount = $validated['discount'];
        $orderDetails->deliveryaddress = $validated['deliveryaddress'];
        $orderDetails->payment_method = $validated['payment_method'];
        $orderDetails->statuspayment = $validated['statuspayment'];
        $orderDetails->mobilenumber = $validated['mobilenumber'];
        $orderDetails->deliverystatus = $validated['deliverystatus'];
        $orderDetails->save();

        return redirect('admin/orderdetails')->with('success', 'Order details updated successfully');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        $orderDetails = OrderDetails::findOrFail($id);
        $orderDetails->delete();

        return redirect('admin/orderdetails')->with('success', 'Order details deleted successfully');
    }
}
