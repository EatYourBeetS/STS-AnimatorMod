package eatyourbeets.cards.animator.beta;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class KotoriKanbe extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 6, 35);
        SetUpgrade(0, 0, 2);
        SetExhaust(true);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.RemovePower(p, m, ArtifactPower.POWER_ID);
        GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        GameActions.Bottom.Heal(p, m, secondaryValue);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.MoveCard(this, player.discardPile)
        .ShowEffect(true, true);

        return true;
    }
}