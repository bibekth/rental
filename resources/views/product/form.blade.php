@extends('layouts.app')
@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Create Product</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error )
        <li style="color: red"> {{$error}} </li>
        @endforeach  
        
    </ul>
        
    @endif

    <form action="{{ route('product.store' )}}" method="POST" enctype="multipart/form-data">
        @csrf
        <div class="sa-text-field-block">
            <label for="">Product Name</label>
            <input type="text" name="name" id="nameid" placeholder="Enter product Name">
        </div>
        <div class="sa-text-field-block">
            <label for="">Description</label>
            <textarea name="description" id="descriptionid" placeholder="Write an description....."></textarea>
        </div>
        <div class="sa-text-field-block">
            <label for="">Amount</label>
            <input type="number" name="amount" id="amountid" placeholder="Enter amount">
        </div>
        <div class="sa-text-field-block sa-text-field-block-file">
            <label for="">Upload Image</label>
            <input type="file" name="photo" id="photoid">
        </div>
        <div class="sa-text-field-block">
            <label for="">Purchase Date</label>
            <input type="date" name="purchase_date" id="purchasedate">
        </div>
        <div class="sa-text-field-block">
            <label for="">Category</label>
            <select name="category_id" id="categoryid"> 
                <option value="" selected disabled>Select an option</option>
                @foreach ($categories as $category)
                <option value="{{ $category-> id }}">{{ $category-> name }}</option>
                @endforeach
                
            </select>
        </div>
        
       
        <div class="sa-form-button">
            <input type="submit" value="Submit">
        </div>
    </form>
</div>
@endsection