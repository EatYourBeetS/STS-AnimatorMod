package eatyourbeets.cards.animator.series.Overlord;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class PandorasActor extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(PandorasActor.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public PandorasActor()
    {
        super(DATA);

        Initialize(0, 4);
        SetUpgrade(0, 2);

        SetSynergy(Synergies.Overlord, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.Callback(__ ->
        {
            AbstractCard copy = this.makeStatEquivalentCopy();
            copy.applyPowers();
            copy.use(AbstractDungeon.player, null);
            copy.purgeOnUse = true;
            copy.freeToPlayOnce = true;
            Synergies.SetLastCardPlayed(copy);
        });

        return true;
    }
}