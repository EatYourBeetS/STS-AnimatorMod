package pinacolada.cards.pcl.series.OnePunchMan;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Saitama extends PCLCard //TODO
{
    public static final PCLCardData DATA = Register(Saitama.class)
            .SetSkill(0, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Saitama(1), false);
                data.AddPreview(new Saitama(2), false);
                data.AddPreview(new Saitama(3), false);
                data.AddPreview(new Saitama(4), false);
                data.AddPreview(new Saitama(5), false);
            });

    private int stage;

    public Saitama()
    {
        this(0);
    }

    private Saitama(int stage)
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Red(2);
        SetAffinity_Green(1);
        SetAffinity_Light(1);

        SetAttackType(PCLAttackType.Normal);
        PCLGameUtilities.ModifyCostForCombat(this, stage, false);
        this.stage = this.misc = stage;
        SetEffect(stage);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        final AbstractAttribute damage = super.GetDamageInfo();
        if (damage != null && stage == 4)
        {
            damage.AddMultiplier(magicNumber);
        }

        return damage;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

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

        Saitama other = PCLJUtils.SafeCast(card, Saitama.class);
        if (other != null)
        {
            other.misc = other.stage = this.misc;
            other.SetEffect(stage);
        }

        return card;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        switch (stage)
        {
            case 0:
            {
                // Do Nothing / Motivate 1
                if (upgraded)
                {
                    PCLActions.Bottom.Motivate();
                }

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                PCLActions.Bottom.Draw(magicNumber);
                PCLActions.Bottom.GainVelocity(secondaryValue);

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                PCLActions.Bottom.StackPower(new BufferPower(p, 1));

                break;
            }

            case 3:
            {
                // Gain !M! Force.
                PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
                PCLActions.Bottom.GainMight(magicNumber);
                PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Red, 1);

                break;
            }

            case 4:
            {
                PCLActions.Bottom.GainVigor(magicNumber);
                PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Red, 2);
                break;
            }

            case 5:
            {
                // Remove Intangible. Deal !D! damage. Stun The Enemy
                PCLActions.Bottom.RemovePower(p, m, IntangiblePower.POWER_ID);
                PCLActions.Bottom.RemovePower(p, m, IntangiblePlayerPower.POWER_ID);
                PCLActions.Bottom.RemovePower(p, m, InvinciblePower.POWER_ID);

                PCLActions.Bottom.VFX(VFX.VerticalImpact(m.hb));
                PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.PUNCH).forEach(d -> d.SetPiercing(true, true));
                PCLActions.Bottom.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);

                PCLActions.Bottom.ApplyPower(p, m, new StunMonsterPower(m, 1));

                break;
            }
        }

        PCLActions.Bottom.ModifyAllInstances(uuid, c ->
        {
            if (c.misc < 5)
            {
                PCLGameUtilities.ModifyCostForCombat(c, 1, true);
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

                LoadImage(null);

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);

                Initialize(0, 0, 2, 2);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                LoadImage("_0");

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);

                Initialize(0, 0, 0, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                LoadImage("_1");

                break;
            }

            case 3:
            {
                // Gain !M! Force. Deal !D! damage
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[2], true);

                Initialize(10, 0, 5, 0);

                AddScaling(PCLAffinity.Red, 3);

                this.attackType = PCLAttackType.Normal;
                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;


                LoadImage("_2");

                break;
            }

            case 4:
            {
                // Deal !D! damage !M! times.
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[3], true);

                Initialize(0, 0, 30, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                LoadImage("_3");

                break;
            }

            case 5:
            {
                // Remove Intangible. Stun the enemy.
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[4], true);

                Initialize(999, 0, 0, 0);

                SetScaling(PCLAffinity.Red, 99);
                SetScaling(PCLAffinity.Green, 99);

                this.attackType = PCLAttackType.Normal;
                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;
                this.cropPortrait = false;

                LoadImage("_4");

                break;
            }
        }
    }
}