const express = require('express')
const app = express()
const bodyParser = require('body-parser')
//const multer = require('multer');
var nodemailer = require('nodemailer');
const bcrypt = require('bcrypt');
//fs = require('fs-extra')
app.use(bodyParser.urlencoded({ extended: true  }))
const port = process.env.PORT || 3000
const mongoClient = require('mongodb').MongoClient
ObjectId = require('mongodb').ObjectId
const url = "mongodb+srv://eduthon:edu@123@cluster0.nuekr.mongodb.net/Eduthon?retryWrites=true&w=majority"
mongoClient.connect(url, { useUnifiedTopology: true },(err, db) => {
    if (err) {
        console.log("error in connecting to mongodb")
    }
    else {
        console.log("Connected")
        const myDb = db.db('Mongodb')
        app.get('/', (req, res) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'text/html');
            res.end('<h1> Smart Education </h1>');
        })
        app.post('/register', (req, res) => {
            const hash = bcrypt.hashSync(req.body.password, 10)
            const newUser = {
                name: req.body.name,
                email: req.body.email,
                phone: req.body.phone,
                password: req.body.password,
                organization: req.body.organization,
                class: req.body.class,
                profession:"Not Set"

            }
            console.log(newUser);
            const query = { email: newUser.email }
            myDb.collection('Login').findOne(query, (err, result) => {
                if (err) {
                    throw err;
                }
                else if (result == null) {
                    myDb.collection('Login').insertOne(newUser, (err, result) => {
                        if (err)
                            throw err;
                        else
                            res.status(200).send()
                    })
                }
                else {
                    res.status(400).send()
                }
            })
        })
        app.post('/login', (req, res) => {
            const query = {
                email: req.body.email
            }
            myDb.collection('Login').findOne(query, (err, result) => {
                if (result != null) {
                    var hash1 = result.password;
                    console.log(hash1+" "+req.body.password)
                    const rs = bcrypt.compareSync(req.body.password, hash1)
                    console.log(rs)
                    if (result.password==req.body.password) {
                        console.log("Yes")
                        res.status(200).send()
                    }
                    else {
                        res.status(404).send()
                    }
                }
                else {
                    res.status(400).send()

                }
            })
        })
        app.get('/details/:email',(req,res)=>{
            console.log(req.params.email)
            myDb.collection('Login').findOne({email:req.params.email},function(err,result){
                if(err)
                    throw err;
                else if(result==null)
                    res.sendStatus(404);
                else
                    res.status(200).json({
                        phone:result.phone,
                        name:result.name,
                        organization:result.organization,
                        profession:result.profession,
                        std:result.class
                    })
            })
        })
        app.post('/update',(req,res)=>{
            const New={
                $set:{
                    phone:req.body.phone,
                    organization:req.body.organization,
                    profession:req.body.profession,
                    class:req.body.class
                }
            }
            myDb.collection('Login').updateOne({email:req.body.email},New,function(err,result){
                if(err)
                    throw err;
                else if(result==null)
                    res.sendStatus(404);
                else
                    res.status(200).send();
            })
        })
        app.post('/resetpassword', (req, res) => {
            const collection2 = myDb.collection('Login')
            collection2.findOne({ email: req.body.email }, function (err, result) {
                if (err) {
                    throw err;
                }
                else if (result != null) {
                    const hash = bcrypt.hashSync(req.body.password, 10)

                    const User = {
                        $set: {
                            password: hash
                        }
                    }
                    collection2.updateOne({ email: req.body.email }, User, (err, result2) => {
                        if (err) {
                            throw err;
                        }
                        if (result2 != null) {
                            res.status(200).send()
                        }

                    })

                }
                else if (result == null) {
                    res.status(404).send();
                }
            })
        })
        app.post('/resetlink', (req, res) => {
            var transporter = nodemailer.createTransport({
                service: 'gmail',
                auth: {
                    user: "abc@gmail.com",
                    pass: 'XXXXXXXX'
                }
            });
            var mailOptions = {
                from: 'abc@gmail.com',
                to: req.body.mail,
                subject: 'Email verification link',
                text: "Code for email verification " + req.body.code
            };
            transporter.sendMail(mailOptions, function (error, info) {
                if (error) {
                    console.log(error);
                    res.sendStatus(400);
                } else {
                    console.log('Email link sent: ' + info.response);
                    res.sendStatus(200);
                }
            });

        })

        app.post('/todoinsert',(req,res)=>{
            const User={
                email:req.body.email,
                title:req.body.title,
                date:req.body.date,
                done:"No"
            }
            myDb.collection('To-Do').insertOne(User,(err,result)=>{
                if(err)
                    throw err;
                else if(result!=null)
                {
                    res.sendStatus(200);
                }
                else
                {
                    res.sendStatus(400);
                }
                
            })
        })
        app.post('/done',(req,res)=>{
            const NEW={
                $set : {
                    done:"Yes"
                }
            }
            myDb.collection('To-Do').updateOne({email:req.body.email,title:req.body.title,date:req.body.date},NEW,function(err,result){
                if(err)
                    throw err;
                else if(result!=null)
                    res.sendStatus(200);
                else
                    res.sendStatus(404);
            })
        })
        app.get('/done/:email',(req,res)=>{
            const query={
                email:req.params.email,
                done:"Yes"
            }
            myDb.collection('To-Do').find(query).toArray(function(err,result){
                if(err)
                    throw err;
                else if(result)
                    res.status(200).send(result)
                else
                    res.sendStatus(404);
            })
        })
        app.get('/notdone/:email',(req,res)=>{
            const query={
                email:req.params.email,
                done:"No"
            }
            myDb.collection('To-Do').find(query).toArray(function(err,result){
                if(err)
                    throw err;
                else if(result)
                {
                    console.log(result)
                    res.status(200).send(result)
                }
                else
                    res.sendStatus(404);
            })
        })
        app.post('/subject', (req, res) => {
            const User = {
                email: req.body.email,
                topic:req.body.topic
            }
           
                    myDb.collection(req.body.subject).insertOne(User,function(err,result2){
                        if(err)
                            throw err;
                        else if(result2!=null)
                            res.sendStatus(200);
                        else
                            res.sendStatus(404);
                    })
                
           
        })
        app.get('/subject/:subject/:email',(req,res)=>{
            myDb.collection(req.params.subject).find({email:req.params.email}).toArray(function(err,result){
                if(err)
                    throw err;
                else if(result!=null)
                    res.status(200).json(result);
                else
                    res.sendStatus(404);
            })
        })


    }


})
app.use(express.json())
app.listen(port, () => {
    console.log("Listening on port " + port)

})
