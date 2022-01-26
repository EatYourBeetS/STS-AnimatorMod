package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.cards.pcl.series.MadokaMagica.NagisaMomoe;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class NagisaMomoe_Charlotte extends PCLCard implements OnPurgeSubscriber
{
    public static final PCLCardData DATA = Register(NagisaMomoe_Charlotte.class)
            .SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Normal, PCLCardTarget.AoE)
            .SetSeries(NagisaMomoe.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public NagisaMomoe_Charlotte()
    {
        super(DATA);

        Initialize(9, 0, 3, 2);
        SetUpgrade(4, 0, 1, 0);

        SetHealing(true);

        SetAffinity_Star(1);
        SetAffinity_Red(0,0,2);
        SetAffinity_Light(0,0,1);
        SetPurge(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.BITE).forEach(d -> d
                .SetDamageEffect((enemy, __) -> PCLGameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb)))
                .AddCallback(enemies ->
                {
                    int healAmount = 0;
                    for (AbstractCreature enemy : enemies)
                    {
                        if (enemy.lastDamageTaken > 0)
                        healAmount += magicNumber;
                    }

                    if (healAmount > 0)
                    {
                        PCLActions.Bottom.Heal(healAmount);
                    }
                }));
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.AllCharacters(),secondaryValue);
    }


    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card.uuid.equals(this.uuid)) {
            PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
        }
    }
}
