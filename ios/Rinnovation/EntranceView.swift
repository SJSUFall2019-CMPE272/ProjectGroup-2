//
//  EntranceView.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 12/4/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class EntranceView: UIView {
    let titleLabel: UILabel = {
        $0.text = "Come inside!"
        $0.font = .systemFont(ofSize: 35)
        return $0
    }(UILabel())
    let enterButton: UIButton = {
        $0.setTitle("Enter", for: .normal)
        $0.backgroundColor = .systemBlue
        $0.layer.cornerRadius = 5
        $0.clipsToBounds = true
        return $0
    }(UIButton())
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor.white.withAlphaComponent(0.7)
        addSubview(titleLabel)
        addSubview(enterButton)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - UIView

extension EntranceView {
    override func layoutSubviews() {
        super.layoutSubviews()
        titleLabel.sizeToFit()
        titleLabel.center = center
        enterButton.sizeToFit()
        enterButton.frame.size.width += 32
        enterButton.center.x = center.x
        enterButton.frame.origin.y = frame.height - 100 - enterButton.frame.height
    }
}
