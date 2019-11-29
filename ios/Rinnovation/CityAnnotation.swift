//
//  CityAnnotation.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/29/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import MapKit

class CityAnnotation: NSObject, MKAnnotation {
    let coordinate: CLLocationCoordinate2D
    let title: String?
    let city: City
    
    init(city: City) {
        title = city.englishName
        coordinate = CLLocationCoordinate2D(latitude: city.lat, longitude: city.lng)
        self.city = city
    }
}
