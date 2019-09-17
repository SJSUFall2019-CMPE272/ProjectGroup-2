# ProjectGroup-2

## Project Ideas

### 1

1. Title: Geoh
2. Description: See answers to your questions on a map.
3. Goal: Help politicians, scientists, and urban planners craft their messages from raw geographical data. They should have a tool for visualizing & predicting the impact of various factors (i.e. [air quality](https://www.epa.gov/outdoor-air-quality-data), [fire incidents](https://fire.ca.gov/incidents/), etc.) on topics that matter to their audience: climate change, public health, and crime (to name just a few). The tool should enable a user to type a natural-language query (i.e. "How will air quality change in San José over the next 30 years?", "Which cities in California will need more firefighters in 5 years?") and see their answers on a map.
4. Technology: We intend to leverage a cloud-based service platform to deploy services and run machine learning workflows; leaning toward AWS. The team has experience in Scala, Java, and Python, so we will likely leverage those skills. For displaying maps, we will leverage an existing toolset (i.e. Mapbox, MapKit JS, etc.). We will focus on exploring new databases (i.e. time-series DBs, CockroachDB) and building our text-to-map NLP engine. We need help making decisions about how to architect the NLP workflows; any advice?

### 2

1. Title: Rinnovation
2. Description: Optimize your home renovation.
3. Goal:
Guide homeowners toward renovations that will yield optimal results for their property. Whether the customer is focused on time, cost, or ROI, empower them to make the best decision based on local and global trends in renovations. Answer questions such as:
* Which room should I update?
* How much should I invest in a renovation project?
* Who should I trust to get the job done on-time?

There is existing work in this area. For instance, OpenDoor has a [tool that calculates which home improvements add the most value](https://www.opendoor.com/w/home-improvement-value-calculator). With the advent of *iBuyers* (corporations attempting to streamline real estate transactions), the demand for a tool which learns and recommends renovation trends in real-time has never been higher. Flush with venture capital, these corporations are able to make real estate purchases at high velocity and scale, thereby saving time and eliminating waste. However, once they have acquired properties, they have assumed a fair amount of risk. They need to optimize their processes to reduce temporal & fiscal costs associated with renovation. We aim to solve that problem.

4. Technology: 

For reference, Opendoor lists many [technologies](https://hackernoon.com/the-stack-that-helped-opendoor-buy-and-sell-over-1b-in-homes-4a2e59fbcea7) they use, such as: Docker, Kubernetes, and Flask. We will employ some common tools, but also intend to explore time-series DBs, Akka, and [PyTorch](https://pytorch.org). We are also open to suggestions so that we can make quick progress and avoid pitfalls.

References:

Data Sets we are investigating:
* <https://towardsdatascience.com/home-remodeling-analysis-turned-excel-data-handling-in-python-e1115f8302e4>
* <https://www.jchs.harvard.edu/research-areas/reports/demographic-change-and-remodeling-outlook>
* <https://www.statista.com/topics/1732/home-improvement>
* <https://reg.hanleywood.com/rf/REMODELING5502017/001/s/SITE/a/4864>
* <http://hinkle-construction.com/project-costs>
* <https://www.nahb.org/en/Products/34320__Remodeling%20Expenditures%20Zip%20Code-Complete%20Dataset%20-%2034320.aspx>
* <https://www.zillow.com/research/data>
* <https://www.zillow.com/research/ztrax>


iBuyer trends:
* <https://www.curbed.com/2019/3/21/18252048/real-estate-house-flipping-zillow-ibuyer-opendoor>
* <https://www.bloomberg.com/news/features/2019-02-14/zillow-wants-to-flip-your-house>
* <https://www.opendoor.com/w/blog/liquidity-modeling-real-estate-survival-analysis>
> At Opendoor, we buy and sell thousands of homes a month. The longer a home remains in our inventory, the more exposure we have to macroeconomic shifts. Moreover, property taxes, home financing, and maintenance fees all increase with time. These costs—which are sometimes referred to as “liquidity risk”—directly impact our unit economics. It is critical that we’re able to understand and predict how long a house will take to sell.
* <https://www.opendoor.com/w/home-improvement-value-calculator>
* <https://www.opendoor.com/w/guides/how-opendoor-calculates-the-value-of-your-home>
* <https://digital.hbs.edu/platform-digit/submission/opendoor-applying-big-data-to-home-selling>
* <https://www.cbinsights.com/research/report/opendoor-real-estate-teardown-expert-intelligence>
* <https://www.marketwatch.com/story/selling-your-home-to-an-ibuyer-could-cost-you-thousands-heres-why-2019-06-11>

### 3

1. Title: Solargy (SOLAR enerGY)
2. Description: Save Fossil Save Money Save Enviornment.
3. Goal: US is second highest energy consumer in the world and out of which more than 77% of the energy is produced by fossil fuels—petroleum, natural gas, and coal, whereas solar energy contributes to only 1.66% of the total. Texas and California accounts for maximum energy consumption in the country. Hot states like these have more than sufficient sunlight to utilize it into energy production. We will help residential people and industrialists evaluate how much they will save financially in ‘n’ no. of years and eventually lead to free usage of electricity after installation cost is recovered. Also we plan to motivate them by showing how much they will be contributing to the environment. We will take into account predicted electricity prices in future. We will also guide people on what power solar panels should be installed based on their energy consumption. 
4. Technology: We plan to use python and its libraries such as numpy and pandas for model generation. Also we plan to use pytorch or tensorflow for deep learning (if required). We will use seaborn for data visualization.

References:
All the datasets are from the authorised govt. website
https://www.eia.gov/electricity/data/state/
https://www.eia.gov/renewable/data.php
https://www.cnrfc.noaa.gov/forecasts.php


### 4

1. Title: Ghar
2. Description: A convinient way for people to decide their housing options.
3. Goal: The goal is to bring handy all the housing options for the people who are in search of a decent house to leave in. As finding a house that is reasonable in price, close to your work place/school is a difficult task. We will be building a tool that takes into consideration your priorities (i.e saving money, saving time, prime loaction, area near to a good public school, willing to share apartment or is a family person) and present to you all the possible options that you can adopt and estimates the total cost that include the housing expenses as well as the travelling expenses per month. Unlike other softwares our tool will not pin point a particular house that u can buy or lease rather it will loacte all the possible areas where you can find the house with your desired options and then use other sites(https://www.zillow.com) to find your dream home.
4. Technology: Various machine learning libraries of Python such as Panda, numPy, sci-kit learn. Database such as Cockroach Db or mango Db. We may use django if a web application is built.

Refrences:
https://www.kaggle.com/camnugent/california-housing-prices
https://www.kaggle.com/farhankarim1/usa-house-prices
https://advocacy.calchamber.com/policy/issues/california-housing-crisis/
