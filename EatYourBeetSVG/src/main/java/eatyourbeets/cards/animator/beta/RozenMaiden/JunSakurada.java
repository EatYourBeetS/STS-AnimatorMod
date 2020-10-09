package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class JunSakurada extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JunSakurada.class)
    		.SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Curse_JunTormented(), false);
    }

    public JunSakurada()
    {
        super(DATA);

        Initialize(0, 6, 2);
        SetUpgrade(0, 1, 0);
        
        SetUnique(true, true);
        SetEthereal(true);
        SetExhaust(true);
        
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public void OnUpgrade()
    {
        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }

        upgradedMagicNumber = true;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.MakeCardInHand(new Curse_JunTormented());

        GameActions.Bottom.StackPower(new JunSakuradaPower(p, magicNumber, this));
    }

    public static class JunSakuradaPower extends AnimatorPower implements OnCardCreatedSubscriber
    {
        private AbstractCard JunToUpgrade = null;
        //upgrade this card when chance of temp card upgrades run out

        public JunSakuradaPower(AbstractCreature owner, int amount, AbstractCard JunToUpgrade)
        {
            super(owner, JunSakurada.DATA);

            this.amount = amount;

            if (JunToUpgrade != null && CombatStats.TryActivateLimited(JunSakurada.DATA.ID))
                this.JunToUpgrade = JunToUpgrade;

            updateDescription();

            CombatStats.onCardCreated.Subscribe(this);
        }

        @Override
        public void updateDescription()
        {
            if (this.JunToUpgrade != null)
            {
                description = FormatDescription(1, amount);
            }
            else
            {
                description = FormatDescription(0, amount);
            }
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle)
        {
            if (!GameUtilities.IsCurseOrStatus(card) && card.canUpgrade())
            {
                if (this.TryActivate())
                {
                    card.upgrade();
                    card.flash();
                    card.update();

                    if (this.amount == 0)
                    {
                        this.CheckAndRemove();
                    }
                }
            }
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            if (JunToUpgrade != null)
            {
                GameActions.Top.ModifyAllInstances(JunToUpgrade.uuid, AbstractCard::upgrade)
                        .IncludeMasterDeck(true).IsCancellable(false);

                GameActions.Bottom.Callback(() ->
                {
                    final float pos_x = (float) Settings.WIDTH / 4f;
                    final float pos_y = (float) Settings.HEIGHT / 2f;

                    GameEffects.TopLevelQueue.ShowCardBriefly(JunToUpgrade.makeStatEquivalentCopy());
                    GameEffects.TopLevelQueue.Add(new UpgradeShineEffect(pos_x, pos_y));
                });
            }
        }

        private void CheckAndRemove()
        {
            if (this.amount == 0)
                this.RemovePower();
        }

        public boolean TryActivate()
        {
            if (this.amount >= 1)
            {
                this.amount --;

                return true;
            }

            return false;
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            JunSakuradaPower other = JUtils.SafeCast(power, JunSakuradaPower.class);

            if (other != null && power.owner == target && this.JunToUpgrade == null)
            {
                this.JunToUpgrade = other.JunToUpgrade;
            }

            super.onApplyPower(power, target, source);
        }
    }
}
