//import React from 'react';
import Myfooter from "./Myfooter"
import Title from "./Title"
import Slider from "./Slider"
import './Title.css'

//import FormContainer from './FormContainer';
//import { render } from "react-dom";
//import React, { Component } from "react";


import React, { Component } from "react";

import FormContainer from './FormContainer';


const styles = {
  fontFamily: "sans-serif",
  textAlign: "center"
};


function LandingPage(){
    return(
        <div>
        <div>
            <Title/>
        </div>
        <div>
            <Slider/>
        </div>
        
        <center>
        <div className="Title">
            <br/>
            <div>Get Started</div>
            <div><FormContainer/></div> 
        </div>
        
        </center>
        
        
        <div>
            <Myfooter/>
        </div>


    </div>
    );
    
}

export default LandingPage;