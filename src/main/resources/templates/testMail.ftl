<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Mail</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f6f6f6;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 600px;
            margin: 30px auto;
            background: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 2px 8px rgba(0,0,0,0.1);
        }
        .header {
            text-align: center;
            padding-bottom: 20px;
        }
        .header h2 {
            color: #2c3e50;
        }
        .content {
            font-size: 15px;
            color: #333;
            line-height: 1.6;
        }
        .footer {
            margin-top: 20px;
            font-size: 12px;
            text-align: center;
            color: #888;
        }
        .btn {
            display: inline-block;
            padding: 10px 15px;
            background: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
    </style>
</head>

<body>

<div class="container">

    <div class="header">

    <img src="cid:Logo" style="width:50px; margin-bottom:10px;" />

    <h2>Welcome to ${company}</h2>
</div>
    <div class="content">
        <p>Hello <b>${username}</b>,</p>

        <p>${message}</p>

        <p>
            We are happy to inform you that your email system is working successfully.
        </p>

       
    </div>

    <div class="footer">
        © ${year} ${company}. All rights reserved.
    </div>

</div>

</body>
</html>