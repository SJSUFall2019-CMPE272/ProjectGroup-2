//import React from 'react';
import Myfooter from "./Myfooter"
import Title from "./Title"
import Slider from "./Slider"
import Inputs from "./Inputs"
//import FormContainer from './FormContainer';
//import { render } from "react-dom";
//import React, { Component } from "react";
import LandingPage from "./LandingPage"

import React, { Component } from "react";
import { render } from "react-dom";
import FormContainer from './FormContainer';
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
const styles = {
  fontFamily: "sans-serif",
  textAlign: "center"
};


function App() {
  return (
    <div>
     
      <Router>
        <React.Fragment>
        
          <Route exact path='/' component={LandingPage} />
          
          <section className='container'>
           
           
            <Switch>
              <Route exact path='/register' component={Title} />

            </Switch>
          </section>
        </React.Fragment>
      </Router>
    
    </div>
    
  );
}



export default App;
