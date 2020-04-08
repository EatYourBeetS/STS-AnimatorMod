package eatyourbeets.cards.animator.beta;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.animator.special.Bienfu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class KotoriKanbe extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);

    static
    {
        DATA.AddPreview(new Chibimoth(), false);
    }

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0,0,-1);
        SetExhaust(true);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int targetMissingHP = m.maxHealth - m.currentHealth;
        int debuffAmount = Math.floorDiv(targetMissingHP, magicNumber);

        GameActions.Bottom.ApplyWeak(p, m, debuffAmount);
        GameActions.Bottom.ApplyVulnerable(p, m, debuffAmount);

        GameActions.Bottom.Heal(p, m, targetMissingHP);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.MoveCard(this, player.discardPile)
                    .ShowEffect(true, true);
        GameActions.Bottom.MakeCardInHand(new Chibimoth());

        return true;
    }
}