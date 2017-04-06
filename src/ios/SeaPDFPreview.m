/********* SeaPDFPreview.m Cordova Plugin Implementation *******/

#import "SeaPDFPreview.h"
#import "PDFViewController.h"


@implementation SeaPDFPreview

-(void)preview:(CDVInvokedUrlCommand *)command{
    CDVPluginResult *result = nil;
    NSDictionary *dictionary = [command.arguments objectAtIndex:0];
    NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
    NSString *type = [dictionary objectForKey:@"type"];
    NSString *filePath = [dictionary objectForKey:@"filePath"];
    NSString *fileName = [dictionary objectForKey:@"fileName"];
    NSString *errorMsg;
    if(type!=nil&&[type length]>0){
        if(filePath!=nil&&[filePath length]>0){
            if(([type isEqualToString:@"online"])||([type isEqualToString:@"local"]&&fileName!=nil&&[fileName length]>0)){
                [data setValue:@"1" forKey:@"code"];
                [data setValue:@"正在预览" forKey:@"msg"];
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:data];
                [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
                [self startPreview:type filePath:filePath fileName:fileName];
                return;
            }else{
                errorMsg = @"PDF文件名不能为空";
            }
        }else{
            errorMsg = @"PDF路径不能为空";
        }
    }else{
        errorMsg = @"预览类型不能为空";
    }
    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorMsg];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)startPreview:(NSString *)type filePath:(NSString *)filePath fileName:(NSString *)fileName{
    PDFViewController *pdfViewController = [[PDFViewController alloc]initWithType:type filePath:filePath fileName:fileName];
    [pdfViewController setModalTransitionStyle:UIModalTransitionStyleCoverVertical]; // 从底部滑入，默认值。
    UIViewController *mainController = [[[[UIApplication sharedApplication]delegate]window]rootViewController];
    [mainController presentViewController:pdfViewController animated:YES completion:^(){
        NSLog(@"打开PDF在线预览");
    }];
}

@end
