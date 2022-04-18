package patches.cardLibrary;

import com.megacrit.cardcrawl.cards.status.Burn;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.unnamed.status.Status_Burn;

public class CardLibraryPatches_Unnamed
{
    public static EYBCardData GetReplacement(String cardID)
    {
        switch (cardID)
        {
//            case AscendersBane.ID: return Curse_AscendersBane.DATA;
//
//            case VoidCard.ID: return Status_Void.DATA;
//            case Slimed.ID: return Status_Slimed.DATA;
//            case Wound.ID: return Status_Wound.DATA;
//            case Dazed.ID: return Status_Dazed.DATA;
              case Burn.ID: return Status_Burn.DATA;
//
//            case Apparition.ID: return SogaNoTojiko.DATA;
//            case Miracle.ID: return Special_Miracle.DATA;
//            case RitualDagger.ID: return Khajiit.DATA;
//            case JAX.ID: return Special_Refrain.DATA;
//            case Bite.ID: return Special_Bite.DATA;
//
//            case Clumsy.ID: return Curse_Clumsy.DATA;
//            case Decay.ID: return Curse_Decay.DATA;
//            case Doubt.ID: return Curse_Doubt.DATA;
//            case Injury.ID: return Curse_Injury.DATA;
//            case Normality.ID: return Curse_Normality.DATA;
//            case Pain.ID: return Curse_Pain.DATA;
//            case Parasite.ID: return Curse_Parasite.DATA;
//            case Regret.ID: return Curse_Regret.DATA;
//            case Shame.ID: return Curse_Shame.DATA;
//            case Writhe.ID: return Curse_Writhe.DATA;

            default: return null;
        }
    }
}
