@extends('layouts.app')
@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Update Product</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error )
        <li style="color: red"> {{$error}} </li>
        @endforeach  
        
    </ul>
        
    @endif

    <form action="{{ route('product.update', $product->id)}}" method="POST" enctype="multipart/form-data">
        @method('PUT')
        @csrf
        <div class="sa-text-field-block">
            <label for="">Product Name</label>
            <input type="text" name="name" id="nameid" placeholder="Enter product Name" value="{{old('name',$product->name)}}">
        </div>
        <div class="sa-text-field-block">
            <label for="">Description</label>
            <textarea name="description" id="descriptionid" placeholder="Write an description.....">{{old('description',$product->description)}}
            </textarea>
        </div>
        <div class="sa-text-field-block">
            <label for="">Amount</label>
            <input type="number" name="amount" id="amountid" placeholder="Enter amount" value="{{old('amount',$product->amount)}}">
        </div>
        <div class="sa-text-field-block sa-text-field-block-file">
            <label for="">Upload Image</label>
            <input type="file" name="photo" id="photoid">
            @if (file_exists(storage_path().'/app/public/'.$product->photo))
                <img src="{{asset('storage/'.$product->photo)}}" width="50px" height="50px">
            @endif
        </div>
        <div class="sa-text-field-block">
            <label for="">Purchase Date</label>
            <input type="date" name="purchase_date" id="purchasedate" value="{{old('purchase_date',$product->purchase_date)}}">
        </div>
        <div class="sa-text-field-block">
            <label for="">Category</label>
            <select name="category_id" id="categoryid"> 
                <option value="" disabled>Select an option</option>
                @foreach ($categories as $category)
                {{-- yo chai ternary operator use gareko, ani tya category ko id sngha match garyo vani dekhauni selected vako natra null --}}
                <option value="{{ $category-> id }}"{{$category->id==$product->category_id?'selected':''}}>{{ $category-> name }}</option>
                @endforeach
                
            </select>
        </div>
        
       
        <div class="sa-form-button">
            <input type="submit" value="Update">
        </div>
    </form>
</div>
@endsection