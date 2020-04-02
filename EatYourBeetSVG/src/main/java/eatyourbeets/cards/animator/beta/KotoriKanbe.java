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

        Initialize(0, 0, 99, 25);
        SetExhaust(true);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int fifthTargetHP = Math.floorDiv(m.maxHealth, 5);

        GameActions.Bottom.RemovePower(p, m, ArtifactPower.POWER_ID);
        GameActions.Bottom.ApplyWeak(p, m, magicNumber);

        GameActions.Bottom.Heal(p, m, fifthTargetHP);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        int totalCards = player.drawPile.size() + player.discardPile.size() + player.hand.size();
        if (totalCards < secondaryValue)
        {
            GameActions.Bottom.MoveCard(this, player.discardPile)
                    .ShowEffect(true, true);
        }

        return true;
    }
}