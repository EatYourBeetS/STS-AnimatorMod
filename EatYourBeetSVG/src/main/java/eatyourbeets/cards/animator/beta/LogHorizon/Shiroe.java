package eatyourbeets.cards.animator.beta.LogHorizon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Shiroe extends AnimatorCard {
    public static final EYBCardData DATA = Register(Shiroe.class).SetAttack(0, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.Normal);

    protected static final Color upgradeGlowColor = AbstractCard.GREEN_BORDER_GLOW_COLOR;

    public Shiroe() {
        super(DATA);

        Initialize(1, 0, 1,4);
        SetUpgrade(1, 0, 0);

        SetUnique(true, true);
        SetExhaust(true);
        SetShapeshifter();
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }

        upgradedMagicNumber = true;
    }

    @Override
    public void triggerOnGlowCheck()
    {
        super.triggerOnGlowCheck();

        if (CombatStats.SynergiesThisTurn().size() >= secondaryValue && !CombatStats.HasActivatedLimited(cardID))
        {
            this.glowColor = upgradeGlowColor;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.ApplyConstricted(p, m, magicNumber);

        if (CombatStats.SynergiesThisTurn().size() >= secondaryValue && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                    .IncludeMasterDeck(true)
                    .IsCancellable(false);
            flash();
        }
    }
}