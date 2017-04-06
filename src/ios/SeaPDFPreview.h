//
//  SeaPDFPreview.h
//  HelloCordova
//
//  Created by administrator on 2017/4/1.
//
//

#import <UIKit/UIKit.h>
#import <Cordova/CDV.h>

@interface SeaPDFPreview : CDVPlugin

-(void)preview:(CDVInvokedUrlCommand *)command;

-(void)startPreview:(NSString *)type filePath:(NSString *)filePath fileName:(NSString *)fileName;

@end
