package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.series.MadokaMagica.NagisaMomoe;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.TargetHelper;

public class NagisaMomoe_Charlotte extends AnimatorCard implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(NagisaMomoe_Charlotte.class)
            .SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL)
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
        SetExhaust(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.BITE).forEach(d -> d
                .SetDamageEffect((enemy, __) -> GameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb)))
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
                        GameActions.Bottom.HealPlayerLimited(this, healAmount);
                    }
                }));
        GameActions.Bottom.ApplyVulnerable(TargetHelper.AllCharacters(),secondaryValue);
    }


    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card.uuid.equals(this.uuid)) {
            GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
        }
    }
}
