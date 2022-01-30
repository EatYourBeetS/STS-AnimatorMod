package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Joker_Arsene;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnCooldownTriggeredSubscriber;
import pinacolada.interfaces.subscribers.OnPCLClickablePowerUsed;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class Joker extends PCLCard
{
    public static final PCLCardData DATA = Register(Joker.class).SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Persona)
            .PostInitialize(data -> data.AddPreview(new Joker_Arsene(), false));
    protected static ArrayList<AbstractCard> chosen;

    public Joker()
    {
        super(DATA);

        Initialize(3, 0, 3, 2);
        SetHitCount(2);

        SetAffinity_Star(1);
        SetAffinity_Red(0,0,1);
        SetAffinity_Green(0,0,1);
        SetAffinity_Blue(0,0,1);

        SetSoul(5, 0, Joker_Arsene::new);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        this.AddScaling(PCLAffinity.Star, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT)
                .forEach(d -> d.SetVFXColor(Color.RED, Color.RED)
                        .SetSoundPitch(0.5f, 1.5f));
        ChooseCard();

        cooldown.ProgressCooldownAndTrigger(m);
    }

    public void ChooseCard() {
        chosen = CombatStats.GetCombatData(cardID, new ArrayList<>());

        final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        RandomizedList<AbstractCard> pool = PCLGameUtilities.GetCardPoolInCombat(CardRarity.COMMON);
        pool.AddAll(PCLGameUtilities.GetCardPoolInCombat(CardRarity.UNCOMMON).GetInnerList());
        if (rng.random(100) < 20) {
            pool.AddAll(PCLGameUtilities.GetCardPoolInCombat(CardRarity.RARE).GetInnerList());
        }

        while (choice.size() < magicNumber && pool.Size() > 0)
        {
            AbstractCard temp = pool.Retrieve(rng);
            if (temp.type == CardType.POWER || temp.purgeOnUse || temp.hasTag(PURGE)) {
                continue;
            }
            temp = temp.makeCopy();
            if (upgraded) {
                temp.upgrade();
            }
            choice.addToTop(temp);
        }

        PCLActions.Bottom.SelectFromPile(name, 1, choice)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        chosen.add(c);
                        PCLActions.Bottom.ApplyPower(new JokerPower(player, c, secondaryValue));
                    }
                });
    }

    public static class JokerPower extends PCLPower implements OnCooldownTriggeredSubscriber, OnPCLClickablePowerUsed
    {
        public AbstractCard card;

        public JokerPower(AbstractPlayer owner, AbstractCard card, int amount)
        {
            super(owner, Joker.DATA);

            Initialize(amount, PowerType.BUFF, true);
            this.card = card;
            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            super.atEndOfTurn(isPlayer);
            ReducePower(1);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onPCLClickablePowerUsed.Subscribe(this);
            PCLCombatStats.onCooldownTriggered.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onPCLClickablePowerUsed.Unsubscribe(this);
            PCLCombatStats.onCooldownTriggered.Unsubscribe(this);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, card != null ? PCLJUtils.ModifyString(card.name, w -> "#y" + w) : "");
        }

        @Override
        public boolean OnCooldownTriggered(AbstractCard card, PCLCardCooldown cooldown) {
            PCLActions.Delayed.PlayCard(this.card, PCLGameUtilities.GetRandomEnemy(true));
            return true;
        }

        @Override
        public boolean OnClickablePowerUsed(PCLClickablePower power, AbstractMonster target) {
            if (power instanceof AbstractPCLAffinityPower) {
                PCLActions.Delayed.PlayCard(this.card, PCLGameUtilities.GetRandomEnemy(true));
            }
            return true;
        }
    }
}