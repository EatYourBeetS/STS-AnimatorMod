package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LestKarr extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LestKarr.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> deck.size() > 16)
            .PostInitialize(data ->
            {
                data.AddPreview(AffinityToken.GetCard(Affinity.Dark), false);
                data.AddPreview(GetClassCard(Burn.ID), false);
            });

    public LestKarr()
    {
        super(DATA);

        Initialize(0, 2, 0);
        SetUpgrade(0, 1, 0);

        SetAffinity_Dark(1, 1, 2);
        SetAffinity_Blue(1, 1, 0);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Flash(this);
        GameActions.Bottom.GainEnergy(1);
        GameActions.Bottom.SFX(SFX.ATTACK_FIRE, 0.75f, 0.75f);
        GameActions.Bottom.MakeCardInDrawPile(GetClassCard(Burn.ID));
        GameActions.Bottom.MakeCardInDiscardPile(GetClassCard(Burn.ID));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (GameUtilities.InStance(NeutralStance.STANCE_ID))
        {
            GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        }
        else
        {
            GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_FAST_1, 0.75f, 0.8f).SetDuration(0.2f, true);
            GameActions.Bottom.ObtainAffinityToken(Affinity.Dark, false);
        }
    }
}