import React from "react"
import makeCarousel from "react-reveal/makeCarousel"
import Slide from "react-reveal/Slide";
import styled, { css } from 'styled-components';

import Icon from './Icon.png'; 

import Icon1 from './Images/1.jpg';
import Icon2 from './Images/2.jpg';
import Icon3 from './Images/3.jpg';
console.log(Icon); 

function Slider(){
  
    const Container = styled.div`
    position: relative;
    overflow: hidden;
    width: 100%;
    height: 450px;
    `;
 
    const CarouselUI = ({ children }) => <Container>{children}</Container>;
    const Carousel = makeCarousel(CarouselUI);
    return(
        <center>
        <Carousel defaultWait={3000} /*wait for 1000 milliseconds*/ >
            
    <Slide right>
      <div>
      <img src={Icon1} alt="Rinnovation" width="90%" height="500"/>
      </div>
    </Slide>
    <Slide right>
      <div>
      <img src={Icon2} alt="Rinnovation" width="90%" height="500"/>
      </div>
    </Slide>
    <Slide right>
      <div>
      <img src={Icon3} alt="Rinnovation" width="90%" height="500"/>
      </div>
    </Slide>
  </Carousel>
  </center>
    );
}

export default Slider