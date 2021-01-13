package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shiroe extends AnimatorCard {
    public static final EYBCardData DATA = Register(Shiroe.class).SetAttack(0, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.Normal);

    public Shiroe() {
        super(DATA);

        Initialize(1, 0, 0,4);
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
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.ApplyConstricted(p, m, magicNumber);

        if (CombatStats.SynergiesThisTurn() >= secondaryValue && CombatStats.TryActivateLimited(cardID))
        {
            final float pos_x = (float) Settings.WIDTH / 4f;
            final float pos_y = (float) Settings.HEIGHT / 2f;

            upgrade();

            player.bottledCardUpgradeCheck(this);

            if (GameEffects.TopLevelQueue.Count() < 5)
            {
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect(pos_x, pos_y));
                GameEffects.TopLevelQueue.ShowCardBriefly(makeStatEquivalentCopy(), pos_x, pos_y);
            }
        }
    }
}