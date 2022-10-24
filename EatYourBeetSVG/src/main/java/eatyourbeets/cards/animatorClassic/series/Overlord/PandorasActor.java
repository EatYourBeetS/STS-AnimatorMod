package eatyourbeets.cards.animatorClassic.series.Overlord;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class PandorasActor extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(PandorasActor.class).SetSeriesFromClassPackage().SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public PandorasActor()
    {
        super(DATA);

        Initialize(0, 4);
        SetUpgrade(0, 2);


        SetShapeshifter();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.SpendEnergy(cost, false).AddCallback(amount ->
            {
                AbstractCard copy = this.makeStatEquivalentCopy();
                copy.applyPowers();
                copy.use(player, null);
                copy.purgeOnUse = true;
                copy.freeToPlayOnce = true;

                CardSeries.SetLastCardPlayed(copy);
                GameActions.Bottom.GainEnergy(amount);
            });
        }
    }
}