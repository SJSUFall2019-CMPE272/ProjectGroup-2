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

TODO: health
