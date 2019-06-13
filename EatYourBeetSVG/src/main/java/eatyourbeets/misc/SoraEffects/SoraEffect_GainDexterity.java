package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.utilities.GameActionsHelper;

public class SoraEffect_GainDexterity extends SoraEffect
{
    public SoraEffect_GainDexterity(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 1;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper.ApplyPower(player, player, new DexterityPower(player, sora.magicNumber), sora.magicNumber);
    }
}