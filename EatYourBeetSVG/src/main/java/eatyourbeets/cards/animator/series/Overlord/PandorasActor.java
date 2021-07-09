package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class PandorasActor extends AnimatorCard
{
    public static final EYBCardData DATA = Register(PandorasActor.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public PandorasActor()
    {
        super(DATA);

        Initialize(0, 4);
        SetUpgrade(0, 2);

        SetSynergy(Synergies.Overlord);
        SetAffinity_Star(1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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

                Synergies.SetLastCardPlayed(copy);
                GameActions.Bottom.GainEnergy(amount);
            });
        }
    }
}