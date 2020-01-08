package eatyourbeets.cards.animator.series.OnePunchMan;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.JavaUtilities;

public class Saitama extends AnimatorCard
{
    public static final String ID = Register(Saitama.class, EYBCardBadge.Special);

    private int stage;

    public Saitama()
    {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);

        Initialize(0, 0);

        this.misc = 0;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (stage != misc)
        {
            stage = misc = Math.max(Math.min(misc, 5), 0);
            SetEffect(stage);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AbstractCard card = super.makeStatEquivalentCopy();

        Saitama other = JavaUtilities.SafeCast(card, Saitama.class);
        if (other != null)
        {
            other.misc = other.stage = this.misc;
            other.SetEffect(stage);
        }

        return card;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        switch (stage)
        {
            case 0:
            {
                // Do Nothing / Motivate 1
                if (upgraded)
                {
                    GameActions.Bottom.Motivate();
                }

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                GameActions.Bottom.Draw(magicNumber);
                GameActions.Bottom.GainAgility(secondaryValue);

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                GameActions.Bottom.StackPower(new BufferPower(p, 1));

                break;
            }

            case 3:
            {
                // Gain !M! Force. Gain !B! Block.
                GameActions.Bottom.GainForce(magicNumber);
                GameActions.Bottom.GainBlock(block);

                break;
            }

            case 4:
            {
                // Deal !D! damage !M! times.
                for (int i = 1; i < this.magicNumber; i++)
                {
                    GameActions.Bottom.Add(new PummelDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                }
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

                break;
            }

            case 5:
            {
                // Remove Intangible. Deal !D! damage. Stun The Enemy
                GameActions.Bottom.RemovePower(p, m, IntangiblePower.POWER_ID);
                GameActions.Bottom.RemovePower(p, m, IntangiblePlayerPower.POWER_ID);
                GameActions.Bottom.RemovePower(p, m, InvinciblePower.POWER_ID);

                GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F));
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE).SetPiercing(true, true);
                GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

                GameActions.Bottom.ApplyPowerSilently(p, m, new StunMonsterPower(m, 1), 1);

                break;
            }
        }

        GameActions.Bottom.ModifyAllCombatInstances(uuid, c ->
        {
            if (c.misc < 5)
            {
                c.misc += 1;
                c.applyPowers();
            }
        });
    }

    private void SetEffect(int stage)
    {
        switch (stage)
        {
            case 0:
            {
                // Do Nothing
                this.cardText.OverrideDescription(null, true);

                Initialize(0, 0, 0, 0);

                this.target = CardTarget.NONE;
                this.type = CardType.SKILL;

                this.loadCardImage(AnimatorResources.GetCardImage(ID));

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);

                Initialize(0, 0, 3, 2);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_0"));

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[1], true);

                Initialize(0, 8, 0, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_1"));

                break;
            }

            case 3:
            {
                // Gain !M! Force. Gain !B! Block
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[2], true);

                Initialize(0, 9, 6, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_2"));

                break;
            }

            case 4:
            {
                // Deal !D! damage !M! times.
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[3], true);

                Initialize(6, 0, 8, 0);

                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;

                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_3"));

                break;
            }

            case 5:
            {
                // Remove Intangible. Deal !D! damage.
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[4], true);

                Initialize(1000, 0, 0, 0);

                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;

                this.loadCardImage(AnimatorResources.GetCardImage(ID + "_4"));

                break;
            }
        }

        this.upgradeBaseCost(stage);
    }
}